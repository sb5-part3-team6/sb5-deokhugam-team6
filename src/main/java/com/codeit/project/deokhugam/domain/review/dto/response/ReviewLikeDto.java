package com.codeit.project.deokhugam.domain.review.dto.response;

import lombok.Builder;

@Builder
public record ReviewLikeDto(
        Long reviewId,
        Long userId,
        boolean liked
) {}
