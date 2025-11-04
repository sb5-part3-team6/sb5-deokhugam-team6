package com.codeit.project.deokhugam.domain.rank.repository;

import java.time.LocalDate;

public interface RankRepositoryCustom {

  void deleteByDateAndTypeDaily(LocalDate date);
}
