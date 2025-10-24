package com.codeit.project.deokhugam.domain.notification.dto;

import com.codeit.project.deokhugam.domain.review.entity.Review;
import com.codeit.project.deokhugam.domain.user.entity.User;
import lombok.Builder;

@Builder
public record NotificationCreateCommand(User reviewOwner, Review review, User reactor,
                                        Integer rank) {

}
