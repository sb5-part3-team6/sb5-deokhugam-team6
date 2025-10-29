package com.codeit.project.deokhugam.domain.notification.exception;

import com.codeit.project.deokhugam.domain.notification.controller.NotificationController;
import com.codeit.project.deokhugam.domain.notification.exception.detail.NotificationInvalidUserException;
import com.codeit.project.deokhugam.domain.notification.exception.detail.NotificationNotFoundException;
import com.codeit.project.deokhugam.global.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice(assignableTypes = NotificationController.class)
public class NotificationExceptionHandler {

  @ExceptionHandler(NotificationInvalidUserException.class)
  public ResponseEntity<ErrorResponse> handleInvalidUserException(
      NotificationInvalidUserException exception) {
    log.error("알림 예외 발생: code={}, message={}", exception.getErrorCode(), exception.getMessage(),
        exception);
    ErrorResponse response = new ErrorResponse(exception, exception.getErrorCode()
                                                                   .getHttpStatus()
                                                                   .value());
    return ResponseEntity.status(exception.getErrorCode()
                                          .getHttpStatus())
                         .body(response);
  }

  @ExceptionHandler(NotificationNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(
      NotificationNotFoundException exception) {
    log.error("알림 예외 발생: code={}, message={}", exception.getErrorCode(), exception.getMessage(),
        exception);
    ErrorResponse response = new ErrorResponse(exception, exception.getErrorCode()
                                                                   .getHttpStatus()
                                                                   .value());
    return ResponseEntity.status(exception.getErrorCode()
                                          .getHttpStatus())
                         .body(response);
  }

  @ExceptionHandler(MissingRequestHeaderException.class)
  public ResponseEntity<ErrorResponse> handleMissingHeader(
      MissingRequestHeaderException exception) {
    log.error("알림 예외 발생: message={}", exception.getMessage(), exception);
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ErrorResponse response = new ErrorResponse(exception, status.value());
    return ResponseEntity.status(status)
                         .body(response);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMismatch(
      MethodArgumentTypeMismatchException exception) {
    log.error("알림 예외 발생: message={}", exception.getMessage(), exception);
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ErrorResponse response = new ErrorResponse(exception, status.value());
    return ResponseEntity.status(status)
                         .body(response);
  }
}