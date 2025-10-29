package com.codeit.project.deokhugam.domain.user.dto.request;

public record PowerUserQueryParams(
        String period,
        String direction,
        String cursor,
        String after,
        Integer limit
) {}
