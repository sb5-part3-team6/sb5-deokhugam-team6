package com.codeit.project.deokhugam.domain.comment.repository;

import com.codeit.project.deokhugam.domain.comment.dto.response.CommentDto;
import com.codeit.project.deokhugam.domain.comment.entity.Comment;
import com.codeit.project.deokhugam.domain.comment.entity.QComment;
import com.codeit.project.deokhugam.domain.user.entity.QUser;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public PageResponse findCommentsByCursor(
            Long reviewId,
            LocalDateTime after,
            Long cursor,
            int limit,
            String direction
    ) {
        QComment comment = QComment.comment;
        QUser user = QUser.user;
        boolean isAsc = "ASC".equalsIgnoreCase(direction);

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(comment.review.id.eq(reviewId));

        builder.and(comment.deletedAt.isNull());

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

        OrderSpecifier<?> order = isAsc ? comment.createdAt.asc() : comment.createdAt.desc();

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

        List<CommentDto> content = comments.stream()
                .map(c -> new CommentDto(
                        c.getId(),
                        c.getReview().getId(),
                        String.valueOf(c.getUser().getId()),
                        c.getUser().getNickname(),
                        c.getContent(),
                        c.getCreatedAt(),
                        c.getUpdatedAt()
                ))
                .collect(Collectors.toList());

        Long totalElements = queryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.review.id.eq(reviewId)
                        .and(comment.deletedAt.isNull()))
                .fetchOne();

        return new PageResponse(
                content,
                nextCursor != null ? String.valueOf(nextCursor) : null,
                nextAfter != null ? nextAfter.toString() : null,
                limit,
                totalElements,
                hasNext
        );
    }
}

