package com.codeit.project.deokhugam.domain.review.repository;

import com.codeit.project.deokhugam.domain.comment.entity.QComment;
import com.codeit.project.deokhugam.domain.rank.entity.QRank;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import com.codeit.project.deokhugam.domain.review.dto.request.ReviewQueryParams;
import com.codeit.project.deokhugam.domain.review.dto.ReviewStatDto;
import com.codeit.project.deokhugam.domain.review.entity.QReview;
import com.codeit.project.deokhugam.domain.review.entity.QReviewLike;
import com.codeit.project.deokhugam.domain.review.entity.Review;
import com.querydsl.core.BooleanBuilder;
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

@RequiredArgsConstructor
@Repository
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public List<Review> list(ReviewQueryParams params) {

    QReview review = QReview.review;
    BooleanBuilder where = new BooleanBuilder();

    if (params.userId() != null && !params.userId()
                                          .toString()
                                          .trim()
                                          .isEmpty()) {
      where.and(review.user.id.eq(params.userId()));
    }

    if (params.bookId() != null && !params.bookId()
                                          .toString()
                                          .trim()
                                          .isEmpty()) {
      where.and(review.book.id.eq(params.bookId()));
    }

    if (params.keyword() != null && !params.keyword()
                                           .trim()
                                           .isEmpty()) {
      BooleanExpression keywordFilter = review.user.nickname.containsIgnoreCase(params.keyword())
                                                            .or(review.content.containsIgnoreCase(
                                                                params.keyword()));
      where.and(keywordFilter);
    }

    if (params.cursor() != null && !params.cursor()
                                          .trim()
                                          .isEmpty()) {
      Long cursor = Long.parseLong(params.cursor());
      where.and(review.id.lt(cursor));
    }

    where.and(review.deletedAt.isNull());

    OrderSpecifier<?> orderSpecifier = makeOrderSpecifier(params.direction(), params.orderBy());

    return queryFactory.selectFrom(review)
                       .where(where)
                       .orderBy(orderSpecifier, review.id.desc())
                       .limit(params.limit() + 1)
                       .fetch();
  }

  public List<Rank> findRanksByType(String type, String direction, int limit) {
    QRank rank = QRank.rank;
    QRank sub = new QRank("sub");

    Order order = "asc".equalsIgnoreCase(direction) ? Order.ASC : Order.DESC;

    var subQuery = JPAExpressions
        .select(sub.targetId, sub.createdAt.max())
        .from(sub)
        .where(sub.target.eq("REVIEW"), sub.type.eq(type))
        .groupBy(sub.targetId);

    return queryFactory
        .selectFrom(rank)
        .where(rank.target.eq("REVIEW"),
            rank.type.eq(type),
            Expressions.list(rank.targetId, rank.createdAt)
                       .in(subQuery)
        )
        .orderBy(new OrderSpecifier<>(order, rank.rankNo))
        .limit(limit + 1)
        .fetch();
  }

  private OrderSpecifier<?> makeOrderSpecifier(String direction, String orderBy) {

    QReview review = QReview.review;

    Order order = "desc".equalsIgnoreCase(direction) ? Order.DESC : Order.ASC;
    OrderSpecifier<?> orderSpecifier;

    switch (orderBy) {
      case "rating" -> orderSpecifier = new OrderSpecifier<>(order, review.rating);
      default -> orderSpecifier = new OrderSpecifier<>(order, review.createdAt);
    }

    return orderSpecifier;
  }

  public List<ReviewStatDto> getStatsByPeriod(RankType type) {
    LocalDateTime startDateTime = null;
    LocalDateTime endDateTime = null;
    LocalDate today = LocalDate.now();

    switch (type) {
      case DAILY -> {
        startDateTime = today.atStartOfDay();
        endDateTime = today.atTime(LocalTime.MAX);
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

    return getReviewStats(startDateTime, endDateTime);
  }

  private List<ReviewStatDto> getReviewStats(LocalDateTime startDateTime,
      LocalDateTime endDateTime) {
    QReview review = QReview.review;
    QReviewLike like = QReviewLike.reviewLike;
    QComment comment = QComment.comment;

    BooleanExpression reviewCondition =
        startDateTime != null && endDateTime != null ? review.createdAt.between(startDateTime,
            endDateTime) : null;
    BooleanExpression likeCondition =
        startDateTime != null && endDateTime != null ? like.createdAt.between(startDateTime,
            endDateTime) : null;
    BooleanExpression commentCondition =
        startDateTime != null && endDateTime != null ? comment.createdAt.between(startDateTime,
            endDateTime) : null;

    return queryFactory.select(
                           Projections.constructor(ReviewStatDto.class, review.id, like.id.countDistinct(),
                               comment.id.countDistinct()))
                       .from(review)
                       .leftJoin(like)
                       .on(like.review.id.eq(review.id)
                                         .and(likeCondition != null ? likeCondition
                                             : like.id.isNotNull()))
                       .leftJoin(comment)
                       .on(comment.review.id.eq(review.id)
                                            .and(commentCondition != null ? commentCondition
                                                : comment.deletedAt.isNull()))
                       .where(reviewCondition != null ? reviewCondition : review.id.isNotNull())
                       .groupBy(review.id)
                       .fetch();
  }
}
