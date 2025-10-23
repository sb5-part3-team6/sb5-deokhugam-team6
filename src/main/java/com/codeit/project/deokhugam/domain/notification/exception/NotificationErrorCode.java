package com.codeit.project.deokhugam.domain.notification.exception;

import com.codeit.project.deokhugam.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum NotificationErrorCode implements ErrorCode {
  NOTIFICATION_INVALID_USER_CREDENTIALS("알림을 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN),
  NOTIFICATION_NOT_FOUND("알림을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  ;

  private String message;
  private HttpStatus httpStatus;

  NotificationErrorCode(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }
}
