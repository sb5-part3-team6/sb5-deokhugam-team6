package com.codeit.project.deokhugam.domain.review.exception;

import com.codeit.project.deokhugam.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReviewErrorCode  implements ErrorCode {
    REVIEW_NOT_FOUND("R001", "존재하지 않는 리뷰 입니다.", HttpStatus.NOT_FOUND),
    REVIEW_ALREADY_EXIST("R002", "리뷰는 한 번만 작성 가능합니다.",  HttpStatus.CONFLICT),
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ReviewErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
