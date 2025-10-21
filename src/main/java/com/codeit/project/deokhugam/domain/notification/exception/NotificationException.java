package com.codeit.project.deokhugam.domain.notification.exception;

import com.codeit.project.deokhugam.global.enums.ErrorCode;
import com.codeit.project.deokhugam.global.exception.DeokhugamException;

public class NotificationException extends DeokhugamException {

  public NotificationException(ErrorCode errorCode) {
    super(errorCode);
  }

  public NotificationException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }
}
