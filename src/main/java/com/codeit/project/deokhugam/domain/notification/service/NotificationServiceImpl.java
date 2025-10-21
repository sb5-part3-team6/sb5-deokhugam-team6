package com.codeit.project.deokhugam.domain.notification.service;

import com.codeit.project.deokhugam.domain.notification.dto.CursorPageResponseNotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationDto;
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
      String cursor, LocalDate after, Integer limit) {
    return null;
  }

  @Override
  @Transactional
  public NotificationDto checkNotificationById(String notificationId, String userId) {

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

    notification.toggleConfirmed();

    return notificationMapper.toDto(notification);
  }

  @Override
  @Transactional
  public void checkAllNotification(String userId) {

    List<Notification> notifications = notificationRepository.findAllByUserId(Long.parseLong(userId));

    for (Notification notification : notifications) {
      notification.toggleConfirmed();
    }
  }
}
