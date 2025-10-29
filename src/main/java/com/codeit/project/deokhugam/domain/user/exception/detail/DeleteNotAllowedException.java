package com.codeit.project.deokhugam.domain.user.exception.detail;

import com.codeit.project.deokhugam.domain.user.exception.UserErrorCode;
import com.codeit.project.deokhugam.domain.user.exception.UserException;

public class DeleteNotAllowedException extends UserException {

  public DeleteNotAllowedException() {
    super(UserErrorCode.DELETE_NOT_ALLOWED);
  }

  public static DeleteNotAllowedException withEmail(String email) {
    DeleteNotAllowedException exception = new DeleteNotAllowedException();
    exception.addDetail("Email", email);
    return exception;
  }
}
