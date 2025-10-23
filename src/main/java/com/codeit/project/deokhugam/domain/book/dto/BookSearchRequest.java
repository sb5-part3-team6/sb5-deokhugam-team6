package com.codeit.project.deokhugam.domain.book.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class BookSearchRequest {
  private String keyword;

  private BookOrderBy orderBy = BookOrderBy.title;

  private Direction direction = Direction.DESC;

  private String cursor;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime after;

  @Min(1) @Max(200)
  private Integer limit = 50;



}
