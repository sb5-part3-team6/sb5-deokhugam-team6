package com.codeit.project.deokhugam.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

  String name();

  String getMessage();

  HttpStatus getHttpStatus();
}