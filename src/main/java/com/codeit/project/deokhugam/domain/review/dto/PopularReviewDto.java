package com.codeit.project.deokhugam.domain.review.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PopularReviewDto(
        Long id,
        Long reviewId,
        Long bookId,
        String bookTitle,
        String bookThumbnailUrl,
        Long userId,
        String userNickname,
        String reviewContent,
        Integer reviewRating,
        String period,
        Integer rank,
        BigDecimal score,
        Integer likeCount,
        Integer commentCount,
        LocalDateTime createdAt
) {}
