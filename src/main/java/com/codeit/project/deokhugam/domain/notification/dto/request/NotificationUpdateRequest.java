package com.codeit.project.deokhugam.domain.notification.dto.request;

import jakarta.validation.constraints.NotNull;

public record NotificationUpdateRequest(@NotNull Boolean confirmed) {

}