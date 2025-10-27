package com.codeit.project.deokhugam.domain.review.repository;

import com.codeit.project.deokhugam.domain.comment.entity.QComment;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import com.codeit.project.deokhugam.domain.review.dto.ReviewStatDto;
import com.codeit.project.deokhugam.domain.review.entity.QReview;
import com.codeit.project.deokhugam.domain.review.entity.QReviewLike;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
public class ReviewStatRepository {

  private final JPAQueryFactory queryFactory;

  public List<ReviewStatDto> getStatsByPeriod(RankType type) {
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
