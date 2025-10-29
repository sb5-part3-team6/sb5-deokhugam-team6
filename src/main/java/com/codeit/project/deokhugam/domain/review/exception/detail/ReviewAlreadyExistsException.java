package com.codeit.project.deokhugam.domain.review.exception.detail;

import com.codeit.project.deokhugam.domain.review.exception.ReviewErrorCode;
import com.codeit.project.deokhugam.domain.review.exception.ReviewException;

public class ReviewAlreadyExistsException extends ReviewException {
    public ReviewAlreadyExistsException() {
        super(ReviewErrorCode.REVIEW_ALREADY_EXIST);
    }
}
