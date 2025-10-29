package com.codeit.project.deokhugam.domain.user.exception.detail;

import com.codeit.project.deokhugam.domain.user.exception.UserErrorCode;
import com.codeit.project.deokhugam.domain.user.exception.UserException;

public class LoginInputInvalidException extends UserException {

  public LoginInputInvalidException() {
    super(UserErrorCode.LOGIN_INPUT_INVALID);
  }

  public static LoginInputInvalidException withEmail(String email) {
    LoginInputInvalidException exception = new LoginInputInvalidException();
    exception.addDetail("Email", email);
    return exception;
  }

  public static LoginInputInvalidException withPassword(String password) {
    LoginInputInvalidException exception = new LoginInputInvalidException();
    exception.addDetail("Password", password);
    return exception;
  }
}
