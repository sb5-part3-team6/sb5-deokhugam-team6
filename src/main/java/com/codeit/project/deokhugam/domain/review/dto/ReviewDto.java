package com.codeit.project.deokhugam.domain.review.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReviewDto(
        String id,
        String bookId,
        String bookTitle,
        String bookThumbnailUrl,
        String userId,
        String userNickname,
        String content,
        Integer rating,
        Integer likeCount,
        Integer commentCount,
        boolean likedByMe,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
