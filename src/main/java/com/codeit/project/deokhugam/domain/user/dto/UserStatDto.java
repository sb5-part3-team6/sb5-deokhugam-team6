package com.codeit.project.deokhugam.domain.user.dto;

import java.math.BigDecimal;

public record UserStatDto(
        Long userId,
        Long likeCount,
        Long commentCount,
        BigDecimal reviewRankScore
) {}