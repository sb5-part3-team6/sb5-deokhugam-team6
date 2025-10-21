package com.codeit.project.deokhugam.domain.notification.exception;

import com.codeit.project.deokhugam.global.enums.ErrorCode;

public class NotificationNotFoundException extends NotificationException {

  public NotificationNotFoundException() {
    super(ErrorCode.NOTIFICATION_NOT_FOUND);
  }

  public static NotificationNotFoundException withId(String id) {
    NotificationNotFoundException exception = new NotificationNotFoundException();
    exception.addDetail("id", id);
    return exception;
  }
}
