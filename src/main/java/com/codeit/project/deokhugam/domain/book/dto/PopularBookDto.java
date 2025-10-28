package com.codeit.project.deokhugam.domain.book.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PopularBookDto(
        Long id,
        Long bookId,
        String title,
        String author,
        String thumbnailUrl,
        String period,
        Integer rank,
        BigDecimal score,
        Long  reviewCount,
        Double rating,
        LocalDateTime createdAt
) {}
