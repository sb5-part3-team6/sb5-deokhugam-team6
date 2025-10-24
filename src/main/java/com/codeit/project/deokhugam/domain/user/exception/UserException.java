package com.codeit.project.deokhugam.domain.user.exception;

import com.codeit.project.deokhugam.global.exception.DeokhugamException;
import com.codeit.project.deokhugam.global.exception.ErrorCode;

public class UserException extends DeokhugamException {
  public UserException(ErrorCode errorCode) {
    super(errorCode);
  }
}
