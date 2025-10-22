package com.codeit.project.deokhugam.domain.notification.service;

import com.codeit.project.deokhugam.domain.notification.dto.CursorPageResponseNotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationUpdateRequest;
import com.codeit.project.deokhugam.domain.notification.entity.Notification;
import com.codeit.project.deokhugam.domain.notification.exception.NotificationInvalidUserException;
import com.codeit.project.deokhugam.domain.notification.exception.NotificationNotFoundException;
import com.codeit.project.deokhugam.domain.notification.mapper.NotificationMapper;
import com.codeit.project.deokhugam.domain.notification.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  @Override
  public CursorPageResponseNotificationDto getNotifications(String userId, String direction,
      LocalDate cursor, LocalDate after, Integer limit) {
    return null;
  }

  @Override
  @Transactional
  public NotificationDto checkNotificationById(String notificationId,
      NotificationUpdateRequest request, String userId) {

    Notification notification = notificationRepository.findById(Long.parseLong(notificationId))
                                                      .orElseThrow(
                                                          () -> NotificationNotFoundException
                                                              .withId(notificationId));

    if (!notification.getUser()
                     .getId()
                     .toString()
                     .equals(userId)) {
      throw NotificationInvalidUserException
          .withNotificationIdAndUserId(notificationId, userId);
    }

    notification.updateConfirmed(request.confirmed());

    return notificationMapper.toDto(notification);
  }

  @Override
  @Transactional
  public void checkAllNotification(String userId) {

    List<Notification> notifications = notificationRepository.findAllByUserId(
        Long.parseLong(userId));

    if (notifications.isEmpty()) {
      throw NotificationInvalidUserException
          .withNotificationIdAndUserId("all", userId);
    }

    for (Notification notification : notifications) {
      notification.updateConfirmed(true);
    }
  }
}
