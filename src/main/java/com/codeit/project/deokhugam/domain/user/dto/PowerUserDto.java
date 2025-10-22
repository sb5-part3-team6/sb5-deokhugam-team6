package com.codeit.project.deokhugam.domain.user.dto;

import com.codeit.project.deokhugam.domain.user.enums.Period;

public record PowerUserDto(
    String userId,
    String nickname,
    Period period,
    String createdAt,
    Integer rank,
    Integer score,
    Integer reviewScoreSum,
    Integer likeCount,
    Integer commentCount
) {

}
