package com.codeit.project.deokhugam.domain.review.exception;

public class ReviewAlreadyExistsException extends ReviewException {
    public ReviewAlreadyExistsException() {
        super(ReviewErrorCode.REVIEW_ALREADY_EXIST);
    }
}
