package com.codeit.project.deokhugam.domain.rank.service;

import com.codeit.project.deokhugam.domain.rank.dto.RankSearchCommand;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RankServiceImpl implements  RankService {

  @Override
  public List<Rank> findRank(RankSearchCommand command) {

    // TODO search 조건에 맞는 Rank return
    // 예 ) target과 type을 지정하면, 해당하는 해당 타입의 랭킹 리스트 반환 USER / DAILY => 유저 일일랭킹
    return List.of();
  }
}
