package com.codeit.project.deokhugam.domain.rank.service;

import com.codeit.project.deokhugam.domain.rank.dto.RankSearchCommand;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import java.util.List;

public interface RankService {

  List<Rank> findRank(RankSearchCommand command);
}
