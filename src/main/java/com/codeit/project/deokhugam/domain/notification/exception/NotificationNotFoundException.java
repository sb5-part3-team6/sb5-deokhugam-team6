package com.codeit.project.deokhugam.domain.notification.exception;

public class NotificationNotFoundException extends NotificationException {

  public NotificationNotFoundException() {
    super(NotificationErrorCode.NOTIFICATION_NOT_FOUND);
  }

  public static NotificationNotFoundException withId(String id) {
    NotificationNotFoundException exception = new NotificationNotFoundException();
    exception.addDetail("id", id);
    return exception;
  }
}