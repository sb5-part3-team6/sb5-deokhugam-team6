package com.codeit.project.deokhugam.domain.user.exception.detail;

import com.codeit.project.deokhugam.domain.user.exception.UserErrorCode;
import com.codeit.project.deokhugam.domain.user.exception.UserException;

public class NicknameDuplicationException extends UserException {

  public NicknameDuplicationException() {
    super(UserErrorCode.NICKNAME_DUPLICATION);
  }

  public static NicknameDuplicationException withNickname(String nickname) {
    NicknameDuplicationException exception = new NicknameDuplicationException();
    exception.addDetail("Nickname", nickname);
    return exception;
  }
}
