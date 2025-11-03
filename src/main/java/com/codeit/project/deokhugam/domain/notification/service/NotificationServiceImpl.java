package com.codeit.project.deokhugam.domain.notification.service;

import com.codeit.project.deokhugam.domain.notification.dto.command.NotificationCreateCommand;
import com.codeit.project.deokhugam.domain.notification.dto.command.NotificationDeleteCommand;
import com.codeit.project.deokhugam.domain.notification.dto.command.NotificationUpdateCommand;
import com.codeit.project.deokhugam.domain.notification.dto.request.NotificationUpdateRequest;
import com.codeit.project.deokhugam.domain.notification.dto.response.NotificationDto;
import com.codeit.project.deokhugam.domain.notification.entity.Notification;
import com.codeit.project.deokhugam.domain.notification.entity.NotificationType;
import com.codeit.project.deokhugam.domain.notification.exception.detail.NotificationInvalidUserException;
import com.codeit.project.deokhugam.domain.notification.exception.detail.NotificationNotFoundException;
import com.codeit.project.deokhugam.domain.notification.mapper.NotificationMapper;
import com.codeit.project.deokhugam.domain.notification.repository.NotificationRepository;
import com.codeit.project.deokhugam.domain.review.entity.Review;
import com.codeit.project.deokhugam.domain.review.repository.ReviewRepository;
import com.codeit.project.deokhugam.domain.user.repository.UserRepository;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final ReviewRepository reviewRepository;
  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  @Override
  public PageResponse getByCursor(String userId, String direction, String cursor, String after,
      Integer limit) {

    Long uid = Long.parseLong(userId);

    int fetchLimit = limit != null ? limit : 20;

    List<Notification> notifications = notificationRepository.findNotificationsByUserId(uid,
        direction, cursor, after, fetchLimit + 1);

    Long total = notificationRepository.countByUserId(uid);

    int idx = notifications.size() > fetchLimit ? fetchLimit - 1 : notifications.size() - 1;
    String nextCursor = notifications.isEmpty() ? null : notifications.get(idx)
                                                                      .getId()
                                                                      .toString();

    boolean hasNext = notifications.size() > fetchLimit;
    if (hasNext) {
      notifications.remove(notifications.size() - 1);
    }

    return PageResponse.builder()
                       .content(notifications.stream()
                                             .map(notificationMapper::toDto)
                                             .toList())
                       .nextCursor(nextCursor)
                       .nextAfter(nextCursor)
                       .size(fetchLimit)
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
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void create(NotificationCreateCommand command) {

    switch (command.type()) {
      case REVIEW_LIKED -> createByLike(command);
      case REVIEW_COMMENTED -> createByComment(command);
      case REVIEW_RANKED -> createByRank(command);
    }
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void delete(NotificationDeleteCommand command) {

    if (command.type() == NotificationType.REVIEW_LIKED) {
      deleteByLike(command);
    } else if (command.type() == NotificationType.REVIEW_COMMENTED) {
      deleteByReview(command);
    }
    switch (command.type()) {
      case REVIEW_LIKED -> deleteByLike(command);
      case REVIEW_COMMENTED -> deleteByReview(command);
      case REVIEW_RANKED -> deleteByRank();
    }
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void update(NotificationUpdateCommand command) {

    String oldContent = formatCommentedContent(command.user()
                                                      .getNickname(), command.oldData());
    String newContent = formatCommentedContent(command.user()
                                                      .getNickname(), command.newData());

    Notification notification = notificationRepository.findByReviewIdAndTypeAndContentIgnoreNewline(
                                                          command.review()
                                                                 .getId(), NotificationType.REVIEW_COMMENTED.name(), oldContent)
                                                      .orElseThrow(
                                                          NotificationNotFoundException::new);

    notification.updateContent(newContent);
  }

  private void createByLike(NotificationCreateCommand command) {

    String content = NotificationType.REVIEW_LIKED.formatContent(command.reactor()
                                                                        .getNickname());
    saveNotification(new Notification(command.review(), command.review()
                                                               .getUser(),
        NotificationType.REVIEW_LIKED.name(), content, false));
  }

  private void createByComment(NotificationCreateCommand command) {

    String content = command.data() != null ? formatCommentedContent(command.reactor()
                                                                            .getNickname(),
        command.data()) : formatCommentedContent(command.reactor()
                                                        .getNickname(), "");
    saveNotification(new Notification(command.review(), command.review()
                                                               .getUser(),
        NotificationType.REVIEW_COMMENTED.name(), content, false));
  }

  private void createByRank(NotificationCreateCommand command) {

    Review review = reviewRepository.findById(command.reviewId())
                                    .orElse(null);
    if (review == null) {
      return;
    }

    String content = NotificationType.REVIEW_RANKED.formatContent();
    saveNotification(
        new Notification(review, review.getUser(), NotificationType.REVIEW_RANKED.name(), content,
            false));
  }

  private String formatCommentedContent(String nickname, String data) {

    return NotificationType.REVIEW_COMMENTED.formatContent(nickname, data != null ? data : "");
  }

  private String getCommentedContent(NotificationDeleteCommand command) {

    return formatCommentedContent(command.reactor()
                                         .getNickname(), command.data());
  }

  private String getLikedContent(NotificationDeleteCommand command) {

    return NotificationType.REVIEW_LIKED.formatContent(command.reactor()
                                                              .getNickname());
  }

  private void saveNotification(Notification notification) {

    notificationRepository.save(notification);
  }

  private void deleteByReview(NotificationDeleteCommand command) {

    Long reviewId = command.review()
                           .getId();
    String type = NotificationType.REVIEW_COMMENTED.name();
    Long count;

    if (Boolean.TRUE.equals(command.only())) {
      count = notificationRepository.deleteByReviewIdAndTypeAndContentIgnoreNewline(reviewId, type,
          getCommentedContent(command));
    } else {
      count = notificationRepository.deleteNotificationsByReviewIdAndType(reviewId, type);
      deleteByLike(command);
    }
    logDelete(reviewId, NotificationType.REVIEW_COMMENTED.name(), count);
  }

  private void deleteByLike(NotificationDeleteCommand command) {

    Long reviewId = command.review()
                           .getId();
    String type = NotificationType.REVIEW_LIKED.name();
    Long count;

    if (Boolean.TRUE.equals(command.only())) {
      count = notificationRepository.deleteByReviewIdAndTypeAndContentIgnoreNewline(reviewId, type,
          getLikedContent(command));
    } else {
      count = notificationRepository.deleteNotificationsByReviewIdAndType(reviewId, type);
    }
    logDelete(reviewId, NotificationType.REVIEW_LIKED.name(), count);
  }


  private void deleteByRank() {

    String type = NotificationType.REVIEW_RANKED.name();
    LocalDate today = LocalDate.now();

    Long count = notificationRepository.deleteByDateAndType(today, type);

    logDelete(0L, NotificationType.REVIEW_RANKED.name(), count);
  }

  private void logDelete(Long reviewId, String type, Long count) {

    log.info("reviewId : {}, {} notifications deleted: {}", reviewId, type, count);
  }
}