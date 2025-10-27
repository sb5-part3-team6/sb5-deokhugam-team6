package com.codeit.project.deokhugam.domain.review.repository;

import com.codeit.project.deokhugam.domain.comment.entity.QComment;
import com.codeit.project.deokhugam.domain.review.dto.ReviewStatDto;
import com.codeit.project.deokhugam.domain.review.entity.QReview;
import com.codeit.project.deokhugam.domain.review.entity.QReviewLike;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReviewStatRepository {

    private final JPAQueryFactory queryFactory;

    public List<ReviewStatDto> getDailyReviewStats() {
        LocalDate today = LocalDate.now();
        LocalDateTime startDateTime = today.atStartOfDay();
        LocalDateTime endDateTime = today.atTime(23, 59, 59);

        return getReviewStats(startDateTime, endDateTime);
    }

    public List<ReviewStatDto> getWeeklyReviewStats() {
        LocalDateTime endDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = endDateTime.minusDays(6).toLocalDate().atStartOfDay();

        return getReviewStats(startDateTime, endDateTime);
    }

    public List<ReviewStatDto> getMonthlyReviewStats() {
        LocalDate today = LocalDate.now();
        LocalDateTime startDateTime = today.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endDateTime = today.atTime(23, 59, 59);

        return getReviewStats(startDateTime, endDateTime);
    }

    private List<ReviewStatDto> getReviewStats(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        QReview review = QReview.review;
        QReviewLike like = QReviewLike.reviewLike;
        QComment comment = QComment.comment;

        return queryFactory
                .select(Projections.constructor(ReviewStatDto.class,
                        review.id,
                        like.id.countDistinct(),
                        comment.id.countDistinct()
                ))
                .from(review)
                .leftJoin(like).on(like.review.id.eq(review.id)
                        .and(like.createdAt.between(startDateTime, endDateTime)))
                .leftJoin(comment).on(comment.review.id.eq(review.id)
                        .and(comment.createdAt.between(startDateTime, endDateTime)))
                .where(review.createdAt.between(startDateTime, endDateTime))
                .groupBy(review.id)
                .fetch();
    }
}
