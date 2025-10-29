package com.codeit.project.deokhugam.domain.notification.exception.detail;

import com.codeit.project.deokhugam.domain.notification.exception.NotificationErrorCode;
import com.codeit.project.deokhugam.domain.notification.exception.NotificationException;

public class NotificationInvalidUserException extends NotificationException {

  public NotificationInvalidUserException() {
    super(NotificationErrorCode.NOTIFICATION_INVALID_USER_CREDENTIALS);
  }

  public static NotificationInvalidUserException withNotificationIdAndUserId(String notificationId,
      String userId) {
    NotificationInvalidUserException exception = new NotificationInvalidUserException();
    exception.addDetail("notificationId", notificationId);
    exception.addDetail("userId", userId);
    return exception;
  }
}