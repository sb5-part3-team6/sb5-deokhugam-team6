package com.codeit.project.deokhugam.domain.notification.service;

import com.codeit.project.deokhugam.domain.notification.dto.CursorPageResponseNotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationUpdateRequest;
import com.codeit.project.deokhugam.domain.notification.entity.Notification;
import com.codeit.project.deokhugam.domain.notification.exception.NotificationInvalidUserException;
import com.codeit.project.deokhugam.domain.notification.exception.NotificationNotFoundException;
import com.codeit.project.deokhugam.domain.notification.mapper.NotificationMapper;
import com.codeit.project.deokhugam.domain.notification.repository.NotificationRepository;
import com.codeit.project.deokhugam.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  @Override
  public CursorPageResponseNotificationDto getByCursor(String userId, String direction,
      LocalDate cursor, LocalDate after, Integer limit) {

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

    return CursorPageResponseNotificationDto.builder()
                                            .content(notifications.stream()
                                                                  .map(notificationMapper::toDto)
                                                                  .toList())
                                            .nextCursor(nextCursor)
                                            .size(notifications.size())
                                            .hasNext(hasNext)
                                            .totalElements(total)
                                            .build();
  }

  @Override
  @Transactional
  public NotificationDto checkById(String notificationId,
      NotificationUpdateRequest request, String userId) {

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
}