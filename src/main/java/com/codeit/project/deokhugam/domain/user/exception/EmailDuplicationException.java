package com.codeit.project.deokhugam.domain.user.exception;

public class EmailDuplicationException extends UserException {

  public EmailDuplicationException() {
    super(UserErrorCode.EMAIL_DUPLICATION);
  }

  public static EmailDuplicationException withEmail(String email) {
    EmailDuplicationException exception = new EmailDuplicationException();
    exception.addDetail("Email", email);
    return exception;
  }
}
