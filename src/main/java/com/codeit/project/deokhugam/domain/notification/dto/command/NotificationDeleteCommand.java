package com.codeit.project.deokhugam.domain.notification.dto.command;

import com.codeit.project.deokhugam.domain.notification.entity.NotificationType;
import com.codeit.project.deokhugam.domain.review.entity.Review;
import com.codeit.project.deokhugam.domain.user.entity.User;
import lombok.Builder;

@Builder
public record NotificationDeleteCommand(
    Review review,
    User reactor,
    NotificationType type,
    String data,
    Boolean only) {

}