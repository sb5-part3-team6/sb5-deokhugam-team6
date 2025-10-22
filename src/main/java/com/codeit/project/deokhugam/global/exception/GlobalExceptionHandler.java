package com.codeit.project.deokhugam.global.exception;

import com.codeit.project.deokhugam.global.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
    ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR.value());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                         .body(errorResponse);
  }

  @ExceptionHandler(DeokhugamException.class)
  public ResponseEntity<ErrorResponse> handleDeokhugamException(DeokhugamException exception) {
    log.error("커스텀 예외 발생: code={}, message={}", exception.getErrorCode(), exception.getMessage(),
        exception);
    ErrorResponse response = new ErrorResponse(exception, exception.getErrorCode()
                                                                   .getHttpStatusCode()
                                                                   .value());
    return ResponseEntity.status(exception.getErrorCode()
                                          .getHttpStatusCode())
                         .body(response);
  }
}