package com.codeit.project.deokhugam.domain.review.dto;

public record ReviewQueryParams(
        Long userId,
        Long bookId,
        String keyword,
        String orderBy,
        String direction,
        String cursor,
        String after,
        Integer limit,
        Long requestUserId
) {}
