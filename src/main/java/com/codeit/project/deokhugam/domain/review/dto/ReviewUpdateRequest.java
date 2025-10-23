package com.codeit.project.deokhugam.domain.review.dto;

public record ReviewUpdateRequest(
        String content,
        Integer rating
) {}
