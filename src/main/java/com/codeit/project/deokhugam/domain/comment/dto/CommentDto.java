package com.codeit.project.deokhugam.domain.comment.dto;

import java.time.LocalDateTime;

public record CommentDto(
        Long id,
        Long reviewId,
        Long userId,
        String userNickname,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
