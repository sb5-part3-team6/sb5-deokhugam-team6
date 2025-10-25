package com.codeit.project.deokhugam.domain.user.dto;

import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import lombok.Builder;

@Builder
public record PowerUserDto(
    String userId,
    String nickname,
    RankType period,
    String createdAt,
    Integer rank,
    Integer score,
    Integer reviewScoreSum,
    Integer likeCount,
    Integer commentCount
) {

}
