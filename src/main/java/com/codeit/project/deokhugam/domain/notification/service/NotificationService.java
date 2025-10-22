package com.codeit.project.deokhugam.domain.notification.service;

import com.codeit.project.deokhugam.domain.notification.dto.CursorPageResponseNotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationUpdateRequest;
import java.time.LocalDate;

public interface NotificationService {

  CursorPageResponseNotificationDto getNotifications(String userId, String direction, String cursor,
      LocalDate after, Integer limit);

  NotificationDto checkNotificationById(String notificationId, NotificationUpdateRequest request, String userId);

  void checkAllNotification(String userId);
}
