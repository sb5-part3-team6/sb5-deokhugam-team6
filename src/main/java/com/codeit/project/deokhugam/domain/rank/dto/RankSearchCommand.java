package com.codeit.project.deokhugam.domain.rank.dto;

import com.codeit.project.deokhugam.domain.rank.entity.RankTarget;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import lombok.Builder;

@Builder
public record RankSearchCommand(RankTarget target, RankType type, String direction, String cusor,
                                String after, Long limit) {

}