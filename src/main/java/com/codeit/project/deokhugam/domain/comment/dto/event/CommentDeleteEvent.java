package com.codeit.project.deokhugam.domain.comment.dto.event;

import com.codeit.project.deokhugam.domain.review.entity.Review;
import com.codeit.project.deokhugam.domain.user.entity.User;

public record CommentDeleteEvent(
    Review review,
    User user,
    String data,
    Boolean only) {

}