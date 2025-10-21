package com.codeit.project.deokhugam.global.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {

  INVALID_USER_CREDENTIALS("잘못된 사용자 인증 정보입니다."),
  NOTIFICATION_NOT_FOUND("알림을 찾을 수 없습니다."),

  INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다."),
  INVALID_REQUEST("잘못된 요청입니다.");;

  private final String message;

  ErrorCode(String message) {
    this.message = message;
  }
}
