package com.codeit.project.deokhugam.domain.user.exception;

import com.codeit.project.deokhugam.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode implements ErrorCode {

  INVALID_INPUT_VALUE("유효하지 않은 입력입니다.", HttpStatus.BAD_REQUEST),

  EMAIL_DUPLICATION("중복된 이메일입니다.", HttpStatus.CONFLICT),
  NICKNAME_DUPLICATION("중복된 닉네임입니다.", HttpStatus.CONFLICT),

  USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

  LOGIN_INPUT_INVALID("이메일 또는 비밀번호를 확인해주세요", HttpStatus.UNAUTHORIZED),

  USER_ALREADY_DELETED("이미 삭제된 사용자입니다.", HttpStatus.CONFLICT),
  DELETE_NOT_ALLOWED("Soft Delete가 수행되지 않았습니다.", HttpStatus.CONFLICT);

  private String message;
  private HttpStatus httpStatus;

  UserErrorCode(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }
}
