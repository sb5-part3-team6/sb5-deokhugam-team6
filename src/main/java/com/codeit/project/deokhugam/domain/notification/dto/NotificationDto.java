package com.codeit.project.deokhugam.domain.notification.dto;

import java.time.LocalDateTime;

public record NotificationDto(String id, String userId, String reviewId, String reviewTitle,
                              String content, Boolean confirmed, LocalDateTime createdAt,
                              LocalDateTime updatedAt) {

}