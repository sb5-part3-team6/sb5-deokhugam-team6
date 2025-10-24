package com.codeit.project.deokhugam.global.common.dto;

import com.codeit.project.deokhugam.global.exception.DeokhugamException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public record ErrorResponse(LocalDateTime timestamp, String code, String message,
                            Map<String, Object> details, String exceptionType, int status) {

  public ErrorResponse {
    if (timestamp == null) {
      timestamp = LocalDateTime.now();
    }
  }

  public ErrorResponse(DeokhugamException exception, int status) {
    this(
        LocalDateTime.now(),
        exception.getErrorCode()
                 .name(),
        exception.getMessage(),
        exception.getDetails(),
        exception.getClass()
                 .getSimpleName(),
        status
    );
  }

  public ErrorResponse(Exception exception, int status) {
    this(
        LocalDateTime.now(),
        exception.getClass()
                 .getSimpleName(),
        "",
        new HashMap<>(),
        exception.getClass()
                 .getSimpleName(),
        status
    );
  }
}