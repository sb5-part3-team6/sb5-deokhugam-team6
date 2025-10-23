package com.codeit.project.deokhugam.domain.user.exception;

public class UserNotFoundException extends UserException {

  public UserNotFoundException() {
    super(UserErrorCode.USER_NOT_FOUND);
  }

  public static UserNotFoundException withId(String id) {
    UserNotFoundException exception = new UserNotFoundException();
    exception.addDetail("Id", id);
    return exception;
  }
}
