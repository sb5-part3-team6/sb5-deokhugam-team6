package com.codeit.project.deokhugam.domain.notification.service;

import com.codeit.project.deokhugam.domain.notification.dto.NotificationCreateCommand;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationDeleteCommand;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationUpdateRequest;
import com.codeit.project.deokhugam.domain.notification.entity.Notification;
import com.codeit.project.deokhugam.domain.notification.entity.NotificationType;
import com.codeit.project.deokhugam.domain.notification.exception.NotificationInvalidUserException;
import com.codeit.project.deokhugam.domain.notification.exception.NotificationNotFoundException;
import com.codeit.project.deokhugam.domain.notification.mapper.NotificationMapper;
import com.codeit.project.deokhugam.domain.notification.repository.NotificationRepository;
import com.codeit.project.deokhugam.domain.user.repository.UserRepository;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  @Override
  public PageResponse getByCursor(String userId, String direction, LocalDate cursor,
      LocalDate after, Integer limit) {

    Long uid = Long.parseLong(userId);

    int fetchLimit = limit != null ? limit : 20;

    List<Notification> notifications = notificationRepository.findNotificationsByUserId(uid,
        direction, cursor, after, fetchLimit);

    Long total = notificationRepository.countByUserId(uid);

    String nextCursor = notifications.isEmpty() ? null : notifications.get(notifications.size() - 1)
                                                                      .getCreatedAt()
                                                                      .toLocalDate()
                                                                      .toString();

    boolean hasNext = notifications.size() >= fetchLimit;

    return PageResponse.builder()
                       .content(notifications.stream()
                                             .map(notificationMapper::toDto)
                                             .toList())
                       .nextCursor(nextCursor)
                       .nextAfter(nextCursor)
                       .size(notifications.size())
                       .hasNext(hasNext)
                       .totalElements(total)
                       .build();
  }

  @Override
  @Transactional
  public NotificationDto checkById(String notificationId, NotificationUpdateRequest request,
      String userId) {

    Long nid = Long.parseLong(notificationId);
    Long uid = Long.parseLong(userId);

    Notification notification = notificationRepository.findById(nid)
                                                      .orElseThrow(
                                                          () -> NotificationNotFoundException.withId(
                                                              notificationId));

    if (!notification.getUser()
                     .getId()
                     .equals(uid)) {
      throw NotificationInvalidUserException.withNotificationIdAndUserId(notificationId, userId);
    }

    notification.updateConfirmed(request.confirmed());

    return notificationMapper.toDto(notification);
  }

  @Override
  @Transactional
  public void checkAll(String userId) {

    Long uid = Long.parseLong(userId);

    if (!userRepository.existsById(uid)) {
      throw NotificationInvalidUserException.withNotificationIdAndUserId("all", userId);
    }

    List<Notification> notifications = notificationRepository.findAllByUserIdAndConfirmedFalse(uid);

    for (Notification notification : notifications) {
      notification.updateConfirmed(true);
    }
  }

  @Override
  public void create(NotificationCreateCommand command) {

    switch (command.type()) {
      case REVIEW_LIKED -> handleReviewLiked(command);
      case REVIEW_COMMENTED -> handleReviewCommented(command);
      case REVIEW_RANKED -> handleReviewRanked(command);
    }
  }

  @Override
  public void delete(NotificationDeleteCommand command){
    // TODO 조금 더 고민하고 구현해야할듯
  }

  private void handleReviewLiked(NotificationCreateCommand command) {

    String content = NotificationType.REVIEW_LIKED.formatContent(command.reactor()
                                                                        .getNickname());
    Notification notification = new Notification(command.review(), command.review().getUser(),
        NotificationType.REVIEW_LIKED.name(), content, false);
    notificationRepository.save(notification);
  }

  private void handleReviewCommented(NotificationCreateCommand command) {

    String content;
    if (command.data() != null) {
      content = NotificationType.REVIEW_COMMENTED.formatContent(command.reactor()
                                                                       .getNickname(),
          command.data());
    } else {
      content = NotificationType.REVIEW_COMMENTED.formatContent(command.reactor()
                                                                       .getNickname(), "");
    }

    Notification notification = new Notification(command.review(), command.review().getUser(),
        NotificationType.REVIEW_COMMENTED.name(), content, false);
    notificationRepository.save(notification);
  }

  private void handleReviewRanked(NotificationCreateCommand command) {
    // TODO 인기 순위 진입 시 알림 메세지 확인
    String content;
    if (command.data() != null) {
      content = String.format("리뷰가 인기 순위 %s위에 들었어요!", command.data());
    } else {
      content = NotificationType.REVIEW_RANKED.formatContent();
    }

    Notification notification = new Notification(command.review(), command.review().getUser(),
        NotificationType.REVIEW_RANKED.name(), content, false);
    notificationRepository.save(notification);
  }
}