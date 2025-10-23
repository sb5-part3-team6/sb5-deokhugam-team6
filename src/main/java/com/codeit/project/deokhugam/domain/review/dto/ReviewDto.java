package com.codeit.project.deokhugam.domain.review.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReviewDto(
        Long id,
        Long bookId,
        String bookTitle,
        String bookThumbnailUrl,
        Long userId,
        String userNickname,
        String content,
        Integer rating,
        Integer likeCount,
        Integer commentCount,
        boolean likedByMe,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
