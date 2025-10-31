package com.codeit.project.deokhugam.domain.review.dto.event;

import com.codeit.project.deokhugam.domain.review.entity.Review;
import com.codeit.project.deokhugam.domain.user.entity.User;

public record ReviewRankedEvent(Review review, User user, String data) {

}
