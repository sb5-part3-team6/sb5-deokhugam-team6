package com.codeit.project.deokhugam.domain.notification.mapper;

import com.codeit.project.deokhugam.domain.notification.dto.response.NotificationDto;
import com.codeit.project.deokhugam.domain.notification.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

  @Mapping(target = "reviewId", source = "review.id")
  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "reviewTitle", source = "review.content")
  NotificationDto toDto(Notification notification);
}