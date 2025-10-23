package com.codeit.project.deokhugam.domain.review.dto;

import lombok.Builder;

@Builder
public record ReviewLikeDto(
        Long reviewId,
        Long userId,
        boolean liked
) {}
