package com.codeit.project.deokhugam.domain.book.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookDto(
  Long id,
  
  String title,

  String author,

  String description,

  String publisher,

  LocalDate publishedDate,

  String isbn,

  String thumbnailUrl,

  Long  reviewCount,

  Double rating,

  LocalDateTime createdAt,

  LocalDateTime updatedAt
) {
}
