package com.codeit.project.deokhugam.domain.notification.dto.command;

import com.codeit.project.deokhugam.domain.review.entity.Review;
import com.codeit.project.deokhugam.domain.user.entity.User;
import lombok.Builder;

@Builder
public record NotificationUpdateCommand(
    Review review,
    User user,
    String oldData,
    String newData) {

}