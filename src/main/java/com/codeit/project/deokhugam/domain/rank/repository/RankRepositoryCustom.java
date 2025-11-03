package com.codeit.project.deokhugam.domain.rank.repository;

import java.time.LocalDate;

public interface RankRepositoryCustom {

  void deleteByDate(LocalDate date);
}
