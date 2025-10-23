package com.codeit.project.deokhugam.domain.book.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;


public record BookSearchRequest(String keyword, String orderBy, String direction,
                                String cursor,
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime after,
                                @Min(1) @Max(200) Integer limit) {

  public BookSearchRequest {
    if (limit == null) {
      limit = 20;
    }
  }
}