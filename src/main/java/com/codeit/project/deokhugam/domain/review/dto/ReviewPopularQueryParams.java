package com.codeit.project.deokhugam.domain.review.dto;

public record ReviewPopularQueryParams(
        String period,
        String direction,
        String cursor,
        String after,
        Integer limit
) {}
