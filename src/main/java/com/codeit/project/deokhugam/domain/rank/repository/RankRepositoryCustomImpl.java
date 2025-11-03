package com.codeit.project.deokhugam.domain.rank.repository;

import com.codeit.project.deokhugam.domain.rank.entity.QRank;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RankRepositoryCustomImpl implements RankRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public void deleteByDateAndTypeDaily(LocalDate date) {
    LocalDateTime start = date.atStartOfDay();
    LocalDateTime end = date.atTime(LocalTime.MAX);

    QRank rank = QRank.rank;

    List<Rank> targets = queryFactory.selectFrom(rank)
                                     .where(rank.type.eq(RankType.DAILY.name())
                                                     .and(rank.createdAt.between(start, end)))
                                     .fetch();

    if (!targets.isEmpty()) {
      queryFactory.delete(rank)
                  .where(rank.in(targets))
                  .execute();
    }
  }
}