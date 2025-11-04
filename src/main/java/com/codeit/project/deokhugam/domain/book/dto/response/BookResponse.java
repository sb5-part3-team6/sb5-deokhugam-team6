package com.codeit.project.deokhugam.domain.book.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder(toBuilder=true)
public record BookResponse(
        String title,
        String author,
        String description,
        String publisher,
        LocalDate publishedDate,
        String isbn,
        String thumbnailImage
) {}
