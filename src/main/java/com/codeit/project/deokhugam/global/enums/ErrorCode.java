package com.codeit.project.deokhugam.global.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
  TEMP("test"),
  ;

  private final String message;

  ErrorCode(String message) {
    this.message = message;
  }
}
