package com.codeit.project.deokhugam.domain.review.dto;

public record ReviewCreateRequest(
        Long bookId,
        Long userId,
        String content,
        Integer rating
) {}
