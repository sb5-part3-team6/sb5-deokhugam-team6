package com.codeit.project.deokhugam.domain.book.exception;

public class BookNotFoundException extends BookException {
    public BookNotFoundException() {
        super(BookErrorCode.BOOK_NOT_FOUND);
    }
}
