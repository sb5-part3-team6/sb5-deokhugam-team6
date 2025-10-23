package com.codeit.project.deokhugam.domain.user.exception;

import com.codeit.project.deokhugam.domain.user.controller.UserController;
import com.codeit.project.deokhugam.global.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = UserController.class)
@Slf4j
public class UserExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
    log.error("회원가입 에러 발생 : message = {}", exception.getMessage(), exception);

    HttpStatus status = HttpStatus.BAD_REQUEST;
    ErrorResponse response = new ErrorResponse(exception, status.value());

    return ResponseEntity.status(status)
        .body(response);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(UserNotFoundException exception) {
    log.error("사용자 찾을 수 없음 : code = {}, message = {}", exception.getErrorCode(), exception.getMessage());

    HttpStatus status = HttpStatus.NOT_FOUND;
    ErrorResponse response = new ErrorResponse(exception, status.value());
    return ResponseEntity.status(status)
        .body(response);
  }

  @ExceptionHandler(UserAlreadyDeletedException.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyDeletedException(UserAlreadyDeletedException exception) {
    log.error("사용자가 이미 삭제됨 : code = {}, message = {}", exception.getErrorCode(), exception.getMessage());

    HttpStatus status = HttpStatus.CONFLICT;
    ErrorResponse response = new ErrorResponse(exception, status.value());
    return ResponseEntity.status(status)
        .body(response);
  }

  @ExceptionHandler(NicknameDuplicationException.class)
  public ResponseEntity<ErrorResponse> handleNicknameDuplicationException(NicknameDuplicationException exception) {
    log.error("중복된 닉네임 : code = {}, message = {}", exception.getErrorCode(), exception.getMessage());

    HttpStatus status = HttpStatus.CONFLICT;
    ErrorResponse response = new ErrorResponse(exception, status.value());
    return ResponseEntity.status(status)
        .body(response);
  }

  @ExceptionHandler(EmailDuplicationException.class)
  public ResponseEntity<ErrorResponse> handleEmailDuplicationException(EmailDuplicationException exception) {
    log.error("중복된 이메일 : code = {}, message = {}", exception.getErrorCode(), exception.getMessage());

    HttpStatus status = HttpStatus.CONFLICT;
    ErrorResponse response = new ErrorResponse(exception, status.value());
    return ResponseEntity.status(status)
        .body(response);
  }

  @ExceptionHandler(DeleteNotAllowedException.class)
  public ResponseEntity<ErrorResponse> handleDeleteNotAllowedException(DeleteNotAllowedException exception) {
    log.error("Soft Delete 되지 않은 사용자 : code = {}, message = {}", exception.getErrorCode(), exception.getMessage() );

    HttpStatus status = HttpStatus.CONFLICT;
    ErrorResponse response = new ErrorResponse(exception, status.value());
    return ResponseEntity.status(status)
        .body(response);
  }

  @ExceptionHandler(LoginInputInvalidException.class)
  public ResponseEntity<ErrorResponse> handleNoSuchElementException(LoginInputInvalidException exception) {
    log.error("로그인 에러 발생 : code = {}, message = {}", exception.getMessage(), exception);

    HttpStatus status = HttpStatus.NOT_FOUND;
    ErrorResponse response = new ErrorResponse(exception, status.value());
    return ResponseEntity.status(status)
        .body(response);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict");
  }
}
