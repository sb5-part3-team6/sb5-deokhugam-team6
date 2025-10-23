package com.codeit.project.deokhugam.domain.book.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@Slf4j
@RestControllerAdvice
public class BoockExceptionHandler {

    @ExceptionHandler(BookException.class)
    public ProblemDetail handleBookException(BookException e) {
        BookErrorCode errorCode = e.getErrorCode();
        log.error(e.getMessage(), e);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(errorCode.getHttpStatus(), e.getMessage());

        pd.setType(URI.create("https://deokhugam.com/errors/" + errorCode.getCode()));
        pd.setTitle(errorCode.name());
        pd.setProperty("code", errorCode.getCode());
        pd.setDetail(errorCode.getMessage());
        pd.setStatus(errorCode.getHttpStatus());

        return pd;
    }
}
