package com.codeit.project.deokhugam.domain.notification.dto;

import jakarta.validation.constraints.NotNull;

public record NotificationUpdateRequest(@NotNull Boolean confirmed) {

}