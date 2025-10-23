package com.codeit.project.deokhugam.domain.review.exception;

public class ReviewNotFoundException extends ReviewException {
    public ReviewNotFoundException() {
        super(ReviewErrorCode.REVIEW_NOT_FOUND);
    }
}
