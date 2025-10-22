package com.codeit.project.deokhugam.domain.book.dto;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;

public record BookDto(
  String id,
  
  String title,

  String author,

  String description,

  String publisher,

  LocalDate publishedDate,

  String isbn,

  String thumbnailUrl,

  Integer  reviewCount,

  Double rating,

  LocalDateTime createdAt,

  LocalDateTime updatedAt
) {
}
