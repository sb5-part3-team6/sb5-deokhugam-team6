package com.codeit.project.deokhugam.domain.notification.exception;

import com.codeit.project.deokhugam.global.enums.ErrorCode;

public class NotificationInvalidUserException extends NotificationException {

  public NotificationInvalidUserException() {
    super(ErrorCode.INVALID_USER_CREDENTIALS);
  }

  public static NotificationInvalidUserException withNotificationIdAndUserId(String notificationId,
      String userId) {
    NotificationInvalidUserException exception = new NotificationInvalidUserException();
    exception.addDetail("notificationId", notificationId);
    exception.addDetail("userId", userId);
    return exception;
  }
}
