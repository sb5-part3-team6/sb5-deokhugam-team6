package com.codeit.project.deokhugam.domain.notification.service;

import com.codeit.project.deokhugam.domain.notification.dto.CursorPageResponseNotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationDto;
import java.time.LocalDate;

public class NotificationServiceImpl implements NotificationService {

  @Override
  public CursorPageResponseNotificationDto getNotifications(String userId, String direction,
      String cursor, LocalDate after, Integer limit) {
    return null;
  }

  @Override
  public NotificationDto updateNotificationById(String notificationId, String userId) {
    return null;
  }

  @Override
  public void updateNotificationAll(String userId) {

  }
}
