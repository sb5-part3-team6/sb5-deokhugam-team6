package com.codeit.project.deokhugam.domain.review.dto.request;

public record ReviewPopularQueryParams(
        String period,
        String direction,
        String cursor,
        String after,
        Integer limit
) {}
