package com.codeit.project.deokhugam.domain.book.exception;

import com.codeit.project.deokhugam.global.enums.ErrorCode;
import com.codeit.project.deokhugam.global.exception.DeokhugamException;

public class BookException extends DeokhugamException {

  public BookException(ErrorCode errorCode) {
    super(errorCode);
  }
}
