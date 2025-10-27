package com.codeit.project.deokhugam.domain.book.dto;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public record BookPopularRequest(
    String period,
    String direction,
    String cursor,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime after,
    Integer limit
) {
  public BookPopularRequest {
    if (limit == null) {
      limit = 50;
    }
    if(period == null) {
      period = "DAILY";
    }
    if(direction == null) {
      direction = "ASC";
    }
  }

}
