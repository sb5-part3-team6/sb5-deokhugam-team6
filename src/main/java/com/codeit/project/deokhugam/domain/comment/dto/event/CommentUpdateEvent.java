package com.codeit.project.deokhugam.domain.comment.dto.event;

import com.codeit.project.deokhugam.domain.review.entity.Review;
import com.codeit.project.deokhugam.domain.user.entity.User;

public record CommentUpdateEvent(
    Review review,
    User user,
    String oldData,
    String newData
) {

}