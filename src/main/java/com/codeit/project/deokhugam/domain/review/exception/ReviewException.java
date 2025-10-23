package com.codeit.project.deokhugam.domain.review.exception;

import com.codeit.project.deokhugam.global.exception.DeokhugamException;
import com.codeit.project.deokhugam.global.exception.ErrorCode;

public class ReviewException extends DeokhugamException {
    public ReviewException(ErrorCode errorCode) {
        super(errorCode);
    }
    public ReviewException(ErrorCode errorCode, Throwable cause) {super(errorCode, cause);}

    @Override
    public ReviewErrorCode getErrorCode() {
        return (ReviewErrorCode) super.getErrorCode();
    }
}
