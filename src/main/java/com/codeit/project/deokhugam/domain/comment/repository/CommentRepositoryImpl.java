package com.codeit.project.deokhugam.domain.comment.repository;

import com.codeit.project.deokhugam.domain.comment.dto.CommentDto;
import com.codeit.project.deokhugam.domain.comment.dto.CursorPageResponseCommentDto;
import com.codeit.project.deokhugam.domain.comment.entity.Comment;
import com.codeit.project.deokhugam.domain.comment.entity.QComment;
import com.codeit.project.deokhugam.domain.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public CursorPageResponseCommentDto findCommentsByCursor(
            Long reviewId,
            LocalDateTime after,
            Long cursor,
            int limit,
            String direction
    ) {
        QComment comment = QComment.comment;
        QUser user = QUser.user;
        boolean isAsc = "ASC".equalsIgnoreCase(direction);

        // 1️⃣ 조건 빌더
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(comment.review.id.eq(reviewId));

        if (after != null && cursor != null) {
            if (isAsc) {
                builder.and(
                        comment.createdAt.gt(after)
                                .or(comment.createdAt.eq(after).and(comment.id.gt(cursor)))
                );
            } else {
                builder.and(
                        comment.createdAt.lt(after)
                                .or(comment.createdAt.eq(after).and(comment.id.lt(cursor)))
                );
            }
        }

        // 2️⃣ 정렬
        OrderSpecifier<?> order = isAsc ? comment.createdAt.asc() : comment.createdAt.desc();

        // 3️⃣ 댓글 조회 (limit + 1) -> 다음 페이지 판단
        List<Comment> comments = queryFactory
                .selectFrom(comment)
                .join(comment.user, user).fetchJoin()
                .where(builder)
                .orderBy(order, isAsc ? comment.id.asc() : comment.id.desc())
                .limit(limit + 1)
                .fetch();

        boolean hasNext = comments.size() > limit;
        if (hasNext) comments = comments.subList(0, limit);

        Long nextCursor = hasNext ? comments.get(comments.size() - 1).getId() : null;
        LocalDateTime nextAfter = hasNext ? comments.get(comments.size() - 1).getCreatedAt() : null;

        // 4️⃣ DTO 변환
        List<CommentDto> content = comments.stream()
                .map(c -> new CommentDto(
                        c.getId(),
                        c.getReview().getId(),
                        c.getUser().getId(),
                        c.getUser().getNickname(),
                        c.getContent(),
                        c.getCreatedAt(),
                        c.getUpdatedAt()
                ))
                .collect(Collectors.toList());

        // 5️⃣ totalElements 계산 (리뷰 전체 댓글 수)
        Long totalElements = queryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.review.id.eq(reviewId))
                .fetchOne();

        return new CursorPageResponseCommentDto(
                content,
                nextCursor != null ? String.valueOf(nextCursor) : null,
                nextAfter,
                limit,
                totalElements,
                hasNext
        );
    }
}

