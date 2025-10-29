package com.codeit.project.deokhugam.domain.review.dto.request;

public record ReviewUpdateRequest(
        String content,
        Integer rating
) {}
