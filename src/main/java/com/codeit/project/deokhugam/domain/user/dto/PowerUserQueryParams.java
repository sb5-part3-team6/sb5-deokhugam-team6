package com.codeit.project.deokhugam.domain.user.dto;

public record PowerUserQueryParams(
        String period,
        String direction,
        String cursor,
        String after,
        Integer limit
) {}
