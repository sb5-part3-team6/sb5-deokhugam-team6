package com.codeit.project.deokhugam.domain.review.repository;

import com.codeit.project.deokhugam.domain.rank.entity.QRank;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.review.dto.ReviewPopularQueryParams;
import com.codeit.project.deokhugam.domain.review.dto.ReviewQueryParams;
import com.codeit.project.deokhugam.domain.review.entity.QReview;
import com.codeit.project.deokhugam.domain.review.entity.Review;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<Review> list(ReviewQueryParams params) {

        QReview review = QReview.review;
        BooleanBuilder where = new BooleanBuilder();

        if(params.userId() != null && !params.userId().toString().trim().isEmpty())
            where.and(review.user.id.eq(params.userId()));

        if(params.bookId() != null && !params.bookId().toString().trim().isEmpty())
            where.and(review.book.id.eq(params.bookId()));

        if(params.keyword() != null && !params.keyword().trim().isEmpty()){
            BooleanExpression keywordFilter = review.user.nickname.containsIgnoreCase(params.keyword())
                            .or(review.content.containsIgnoreCase(params.keyword()));
            where.and(keywordFilter);
        }

        OrderSpecifier<?> orderSpecifier = makeOrderSpecifier(params.direction(), params.orderBy());

        return queryFactory.selectFrom(review)
                .where(where)
                .orderBy(orderSpecifier)
                .limit(params.limit() + 1)
                .fetch();
    }

    public List<Rank> findRanksByType(String type, String direction, int limit) {
        QRank rank = QRank.rank;

        Order order = "asc".equalsIgnoreCase(direction) ? Order.ASC : Order.DESC;

        return queryFactory
                .selectFrom(rank)
                .where(rank.target.eq("REVIEW")
                        .and(rank.type.eq(type)))
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
}
