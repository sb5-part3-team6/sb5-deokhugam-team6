package com.codeit.project.deokhugam.domain.notification.service;

import com.codeit.project.deokhugam.domain.notification.dto.CursorPageResponseNotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationUpdateRequest;
import java.time.LocalDate;

public interface NotificationService {

  CursorPageResponseNotificationDto getByCursor(String userId, String direction, LocalDate cursor,
      LocalDate after, Integer limit);

  NotificationDto checkById(String notificationId, NotificationUpdateRequest request, String userId);

  void checkAll(String userId);
}