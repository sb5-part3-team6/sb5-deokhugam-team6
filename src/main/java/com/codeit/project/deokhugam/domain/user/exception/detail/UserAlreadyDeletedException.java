package com.codeit.project.deokhugam.domain.user.exception.detail;

import com.codeit.project.deokhugam.domain.user.exception.UserErrorCode;
import com.codeit.project.deokhugam.domain.user.exception.UserException;

public class UserAlreadyDeletedException extends UserException {

  public UserAlreadyDeletedException() {
    super(UserErrorCode.USER_ALREADY_DELETED);
  }

  public static UserAlreadyDeletedException withEmail(String email) {
    UserAlreadyDeletedException exception = new UserAlreadyDeletedException();
    exception.addDetail("Email", email);
    return exception;
  }
}
