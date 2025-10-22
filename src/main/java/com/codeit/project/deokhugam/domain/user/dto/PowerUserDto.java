package com.codeit.project.deokhugam.domain.user.dto;

import com.codeit.project.deokhugam.domain.rank.entity.RankType;

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
