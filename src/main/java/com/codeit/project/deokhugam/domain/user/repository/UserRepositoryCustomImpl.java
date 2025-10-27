package com.codeit.project.deokhugam.domain.user.repository;

import com.codeit.project.deokhugam.domain.comment.entity.QComment;
import com.codeit.project.deokhugam.domain.rank.entity.QRank;
import com.codeit.project.deokhugam.domain.rank.entity.RankTarget;
import com.codeit.project.deokhugam.domain.review.entity.QReview;
import com.codeit.project.deokhugam.domain.review.entity.QReviewLike;
import com.codeit.project.deokhugam.domain.user.dto.UserStatDto;
import com.codeit.project.deokhugam.domain.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<UserStatDto> getDailyStats() {
    LocalDate yesterday = LocalDate.now()
                                   .minusDays(1);
    LocalDateTime startDateTime = yesterday.atStartOfDay();
    LocalDateTime endDateTime = yesterday.atTime(LocalTime.MAX);

    return getStats(startDateTime, endDateTime);
  }

  @Override
  public List<UserStatDto> getWeeklyStats() {
    // TODO 주간 날짜 통합
    LocalDate yesterday = LocalDate.now()
                                   .minusWeeks(1);
    LocalDateTime startDateTime = yesterday.atStartOfDay();
    LocalDateTime endDateTime = yesterday.atTime(LocalTime.MAX);

    return getStats(startDateTime, endDateTime);
  }

  @Override
  public List<UserStatDto> getMonthlyStats() {
    LocalDate today = LocalDate.now();
    LocalDateTime startDateTime = today.withDayOfMonth(1)
                                       .atStartOfDay();
    LocalDateTime endDateTime = today.atTime(LocalTime.MAX);

    return getStats(startDateTime, endDateTime);
  }


  private List<UserStatDto> getStats(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    QUser user = QUser.user;
    QReviewLike like = QReviewLike.reviewLike;
    QComment comment = QComment.comment;
    QReview review = QReview.review;
    QRank rank = QRank.rank;

    return jpaQueryFactory.select(
                              Projections.constructor(UserStatDto.class, user.id, like.id.countDistinct(),
                                  comment.id.countDistinct(), rank.score.avg()))
                          .from(user)
                          .innerJoin(like)
                          .on(like.user.id.eq(user.id)
                                          .and(like.createdAt.between(startDateTime, endDateTime)))
                          .innerJoin(comment)
                          .on(comment.user.id.eq(user.id)
                                             .and(comment.createdAt.between(startDateTime,
                                                 endDateTime)))
                          .innerJoin(review)
                          .on(review.user.id.eq(user.id)
                                            .and(comment.createdAt.between(startDateTime,
                                                endDateTime)))
                          .innerJoin(rank)
                          .on(rank.targetId.eq(review.id)
                                           .and(rank.target.eq(RankTarget.REVIEW.name()))
                                           .and(rank.createdAt.between(startDateTime, endDateTime)))

                          .where(user.createdAt.between(startDateTime, endDateTime))
                          .groupBy(user.id)
                          .fetch();
  }
}