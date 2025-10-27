package com.codeit.project.deokhugam.domain.review.dto;

import lombok.Builder;

@Builder
public record ReviewStatDto(
        Long reviewId,
        Long likeCount,
        Long commentCount
) {}
