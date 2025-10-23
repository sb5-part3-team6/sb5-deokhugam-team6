package com.codeit.project.deokhugam.domain.notification.service;

import com.codeit.project.deokhugam.domain.notification.dto.NotificationCreateCommand;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationUpdateRequest;
import com.codeit.project.deokhugam.domain.notification.entity.NotificationType;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import java.time.LocalDate;

public interface NotificationService {

  PageResponse getByCursor(String userId, String direction, LocalDate cursor,
      LocalDate after, Integer limit);

  NotificationDto checkById(String notificationId, NotificationUpdateRequest request,
      String userId);

  void checkAll(String userId);

  void createNotification(NotificationType type, NotificationCreateCommand command);
}