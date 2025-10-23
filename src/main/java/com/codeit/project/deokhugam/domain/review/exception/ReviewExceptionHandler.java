package com.codeit.project.deokhugam.domain.review.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@Slf4j
@RestControllerAdvice
public class ReviewExceptionHandler {

    @ExceptionHandler(ReviewException.class)
    public ProblemDetail handleReviewException(ReviewException exception) {
        ReviewErrorCode errorCode = exception.getErrorCode();
        log.error(exception.getMessage(), exception);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(errorCode.getHttpStatus(), errorCode.getMessage());

        pd.setType(URI.create("https://deokhugam.com/errors/" + errorCode.getCode()));
        pd.setTitle(errorCode.name());
        pd.setProperty("code", errorCode.getCode());
        pd.setDetail(errorCode.getMessage());
        pd.setStatus(errorCode.getHttpStatus());

        return pd;
    }

}
