package com.codeit.project.deokhugam.domain.notification.dto.command;

import com.codeit.project.deokhugam.domain.comment.entity.Comment;
import com.codeit.project.deokhugam.domain.notification.entity.NotificationType;
import com.codeit.project.deokhugam.domain.review.entity.Review;

public record NotificationDeleteCommand(Review review, Comment comment,  NotificationType type) {

}