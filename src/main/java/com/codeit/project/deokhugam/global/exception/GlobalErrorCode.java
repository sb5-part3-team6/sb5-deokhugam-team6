package com.codeit.project.deokhugam.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GlobalErrorCode implements ErrorCode {

  INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);

  private final String message;
  private final HttpStatus httpStatus;

  GlobalErrorCode(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }
}