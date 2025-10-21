package com.codeit.project.deokhugam.domain.book.dto;

import java.time.Instant;
import java.util.Date;

public record BookDto(
  String id,
  
  String title,

  String author,

  String description,

  String publisher,

  Date publishedDate,

  String isbn,

  String thumbnailUrl,

  Integer  reviewCount,

  Double rating,

  Instant createAt,

  Instant updateAt
) {

}
