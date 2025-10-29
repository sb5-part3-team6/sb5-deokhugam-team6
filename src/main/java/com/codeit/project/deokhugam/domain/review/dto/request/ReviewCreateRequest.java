package com.codeit.project.deokhugam.domain.review.dto.request;

public record ReviewCreateRequest(
        Long bookId,
        Long userId,
        String content,
        Integer rating
) {}
