package com.codeit.project.deokhugam.domain.book.exception.detail;

import com.codeit.project.deokhugam.domain.book.exception.BookErrorCode;
import com.codeit.project.deokhugam.domain.book.exception.BookException;

public class BookNotFoundException extends BookException {
    public BookNotFoundException() {
        super(BookErrorCode.BOOK_NOT_FOUND);
    }
}
