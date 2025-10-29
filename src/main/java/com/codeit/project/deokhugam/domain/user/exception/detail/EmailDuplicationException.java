package com.codeit.project.deokhugam.domain.user.exception.detail;

import com.codeit.project.deokhugam.domain.user.exception.UserErrorCode;
import com.codeit.project.deokhugam.domain.user.exception.UserException;

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
