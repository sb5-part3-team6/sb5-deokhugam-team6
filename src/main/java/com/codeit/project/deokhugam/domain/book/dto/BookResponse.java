package com.codeit.project.deokhugam.domain.book.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BookResponse(
        String title,
        String author,
        String description,
        String publisher,
        LocalDate publishedDate,
        String isbn,
        String thumbnailImage
) {}
