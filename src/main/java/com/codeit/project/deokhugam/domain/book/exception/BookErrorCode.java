package com.codeit.project.deokhugam.domain.book.exception;

import com.codeit.project.deokhugam.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BookErrorCode implements ErrorCode {
    BOOK_NOT_FOUND("B001", "존재하지 않는 도서 입니다.", HttpStatus.NOT_FOUND);
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}