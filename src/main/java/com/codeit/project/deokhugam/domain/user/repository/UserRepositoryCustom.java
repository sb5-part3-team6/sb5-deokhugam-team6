package com.codeit.project.deokhugam.domain.user.repository;

import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import com.codeit.project.deokhugam.domain.user.dto.UserStatDto;
import java.util.List;

public interface UserRepositoryCustom {

  List<UserStatDto> getStatsByPeriod(RankType type);
}