package com.codeit.project.deokhugam.domain.notification.service;

import com.codeit.project.deokhugam.domain.notification.dto.command.NotificationCreateCommand;
import com.codeit.project.deokhugam.domain.notification.dto.command.NotificationDeleteCommand;
import com.codeit.project.deokhugam.domain.notification.dto.command.NotificationUpdateCommand;
import com.codeit.project.deokhugam.domain.notification.dto.request.NotificationUpdateRequest;
import com.codeit.project.deokhugam.domain.notification.dto.response.NotificationDto;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;

public interface NotificationService {

  PageResponse getByCursor(String userId, String direction, String cursor,
      String after, Integer limit);

  NotificationDto checkById(String notificationId, NotificationUpdateRequest request,
      String userId);

  void checkAll(String userId);

  void create(NotificationCreateCommand command);

  void delete(NotificationDeleteCommand command);

  void update(NotificationUpdateCommand command);
}