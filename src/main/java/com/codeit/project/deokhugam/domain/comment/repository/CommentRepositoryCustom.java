package com.codeit.project.deokhugam.domain.comment.repository;

import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import java.time.LocalDateTime;

public interface CommentRepositoryCustom {
    PageResponse findCommentsByCursor(
            Long reviewId,
            LocalDateTime after,
            Long cursor,
            int limit,
            String direction
    );
}
