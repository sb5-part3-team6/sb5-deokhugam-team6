package com.codeit.project.deokhugam.domain.user.repository;

import com.codeit.project.deokhugam.domain.comment.entity.QComment;
import com.codeit.project.deokhugam.domain.rank.entity.QRank;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.entity.RankTarget;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import com.codeit.project.deokhugam.domain.review.entity.QReview;
import com.codeit.project.deokhugam.domain.review.entity.QReviewLike;
import com.codeit.project.deokhugam.domain.user.dto.UserStatDto;
import com.codeit.project.deokhugam.domain.user.entity.QUser;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<UserStatDto> getStatsByPeriod(RankType type) {
    LocalDateTime startDateTime = null;
    LocalDateTime endDateTime = null;

    LocalDate today = LocalDate.now();

    switch (type) {
      case DAILY -> {
        LocalDate yesterday = today.minusDays(1);
        startDateTime = yesterday.atStartOfDay();
        endDateTime = yesterday.atTime(LocalTime.MAX);
      }
      case WEEKLY -> {
        LocalDate startOfLastWeek = today.with(DayOfWeek.MONDAY);
        startDateTime = startOfLastWeek.atStartOfDay();
        endDateTime = today.atTime(LocalTime.MAX);
      }
      case MONTHLY -> {
        LocalDate firstDayOfLastMonth = today.withDayOfMonth(1);
        startDateTime = firstDayOfLastMonth.atStartOfDay();
        endDateTime = today.atTime(LocalTime.MAX);
      }
      case ALL_TIME -> {
        startDateTime = null;
        endDateTime = null;
      }
    }

    return getStats(startDateTime, endDateTime);
  }

  private List<UserStatDto> getStats(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    QUser user = QUser.user;
    QReviewLike like = QReviewLike.reviewLike;
    QComment comment = QComment.comment;
    QReview review = QReview.review;
    QRank rank = QRank.rank;

    BooleanExpression likeCondition =
        (startDateTime != null && endDateTime != null) ? like.createdAt.between(startDateTime,
            endDateTime) : null;
    BooleanExpression commentCondition =
        (startDateTime != null && endDateTime != null) ? comment.createdAt.between(startDateTime,
            endDateTime) : null;
    BooleanExpression reviewCondition =
        (startDateTime != null && endDateTime != null) ? review.createdAt.between(startDateTime,
            endDateTime) : null;
    BooleanExpression rankCondition =
        (startDateTime != null && endDateTime != null) ? rank.createdAt.between(startDateTime,
            endDateTime) : null;

    return queryFactory.select(
                              Projections.constructor(UserStatDto.class, user.id, like.id.countDistinct(),
                                  comment.id.countDistinct(), rank.score.avg()))
                          .from(user)
                          .leftJoin(like)
                          .on(like.user.id.eq(user.id)
                                          .and(likeCondition != null ? likeCondition
                                              : like.id.isNotNull()))
                          .leftJoin(comment)
                          .on(comment.user.id.eq(user.id)
                                             .and(comment.deletedAt.isNull())
                                             .and(commentCondition != null ? commentCondition
                                                 : comment.deletedAt.isNotNull()))
                          .leftJoin(review)
                          .on(review.user.id.eq(user.id)
                                            .and(review.deletedAt.isNull())
                                            .and(reviewCondition != null ? reviewCondition
                                                : review.id.isNotNull()))
                          .innerJoin(rank)
                          .on(rank.targetId.eq(review.id)
                                           .and(rank.target.eq(RankTarget.REVIEW.name()))
                                           .and(rankCondition != null ? rankCondition
                                               : rank.id.isNotNull()))
                          .where(user.deletedAt.isNull())
                          .groupBy(user.id)
                          .fetch();
  }

  @Override
  public Long deleteExpiredSoftDeletedUsers() {
    QUser user = QUser.user;
    return queryFactory.delete(user)
                        .where(user.deletedAt.before(LocalDateTime.now().minusDays(1)))
                        .execute();
  }

  @Override
  public List<Rank> findRankByType(String type, String direction, int limit) {
    QRank rank = QRank.rank;
    QRank sub = new QRank("sub");

    Order order = "asc".equalsIgnoreCase(direction) ? Order.ASC : Order.DESC;

    var subQuery = JPAExpressions
            .select(sub.targetId, sub.createdAt.max())
            .from(sub)
            .where(sub.target.eq("USER"), sub.type.eq(type))
            .groupBy(sub.targetId);

    return queryFactory
            .selectFrom(rank)
            .where(rank.target.eq("USER"),
                    rank.type.eq(type),
                    Expressions.list(rank.targetId, rank.createdAt).in(subQuery)
            )
            .orderBy(new OrderSpecifier<>(order, rank.rankNo))
            .limit(limit + 1)
            .fetch();
  }
}