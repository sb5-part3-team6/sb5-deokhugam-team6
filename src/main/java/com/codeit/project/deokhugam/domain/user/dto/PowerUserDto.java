package com.codeit.project.deokhugam.domain.user.dto;

import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PowerUserDto(
    String userId,
    String nickname,
    String period,
    String createdAt,
    Integer rank,
    BigDecimal score,
    double reviewScoreSum,
    Integer likeCount,
    Integer commentCount
) {

}
