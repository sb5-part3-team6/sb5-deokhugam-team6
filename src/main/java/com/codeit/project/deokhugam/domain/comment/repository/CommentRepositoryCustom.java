package com.codeit.project.deokhugam.domain.comment.repository;

import com.codeit.project.deokhugam.domain.comment.dto.CursorPageResponseCommentDto;

import java.time.LocalDateTime;

public interface CommentRepositoryCustom {
    CursorPageResponseCommentDto findCommentsByCursor(
            Long reviewId,
            LocalDateTime after,
            Long cursor,
            int limit,
            String direction
    );
}
