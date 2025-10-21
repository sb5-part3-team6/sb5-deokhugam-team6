package com.codeit.project.deokhugam.domain.notification.dto;

import java.util.List;

public record CursorPageResponseNotificationDto(List<NotificationDto> content, String nextCursor,
                                                Integer size, Boolean hasNext, Long totalElements) {

}
