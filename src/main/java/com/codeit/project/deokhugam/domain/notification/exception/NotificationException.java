package com.codeit.project.deokhugam.domain.notification.exception;

import com.codeit.project.deokhugam.global.exception.DeokhugamException;
import com.codeit.project.deokhugam.global.exception.ErrorCode;

public class NotificationException extends DeokhugamException {

  public NotificationException(ErrorCode errorCode) {
    super(errorCode);
  }

  public NotificationException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }
}