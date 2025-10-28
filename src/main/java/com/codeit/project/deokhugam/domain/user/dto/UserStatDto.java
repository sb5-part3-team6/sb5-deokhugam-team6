package com.codeit.project.deokhugam.domain.user.dto;

public record UserStatDto(
        Long userId,
        Long likeCount,
        Long commentCount,
        Double reviewRankScore
) {}