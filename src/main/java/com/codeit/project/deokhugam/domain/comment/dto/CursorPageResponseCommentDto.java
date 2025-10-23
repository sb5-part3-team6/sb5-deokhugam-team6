package com.codeit.project.deokhugam.domain.comment.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CursorPageResponseCommentDto(
        List<CommentDto> content,
        String nextCursor,
        LocalDateTime nextAfter,
        Integer size,
        Long totalElements,
        boolean hasNext
) { }
