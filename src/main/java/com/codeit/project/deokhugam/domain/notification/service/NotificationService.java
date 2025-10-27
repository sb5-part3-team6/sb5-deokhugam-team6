package com.codeit.project.deokhugam.domain.notification.service;

import com.codeit.project.deokhugam.domain.notification.dto.NotificationCreateCommand;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationDeleteCommand;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationUpdateRequest;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;

public interface NotificationService {

  PageResponse getByCursor(String userId, String direction, String cursor,
      String after, Integer limit);

  NotificationDto checkById(String notificationId, NotificationUpdateRequest request,
      String userId);

  void checkAll(String userId);

  void create(NotificationCreateCommand command);

  void delete(NotificationDeleteCommand command);
}