package com.codeit.project.deokhugam.domain.review.exception.detail;

import com.codeit.project.deokhugam.domain.review.exception.ReviewErrorCode;
import com.codeit.project.deokhugam.domain.review.exception.ReviewException;

public class ReviewNotFoundException extends ReviewException {
    public ReviewNotFoundException() {
        super(ReviewErrorCode.REVIEW_NOT_FOUND);
    }
}
