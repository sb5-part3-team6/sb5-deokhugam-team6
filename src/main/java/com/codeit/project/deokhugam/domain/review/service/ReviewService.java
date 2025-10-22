package com.codeit.project.deokhugam.domain.review.service;

import com.codeit.project.deokhugam.domain.review.dto.*;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;

public interface ReviewService {
    ReviewDto create(ReviewCreateRequest request);
    ReviewLikeDto createLike(Long reviewId, Long userId);
    PageResponse list(ReviewQueryParams params, Long userId);
    ReviewDto update(Long reviewId, Long userId, ReviewUpdateRequest request);
    void softDelete(Long reviewId, Long userId);
    void hardDelete(Long reviewId, Long userId);
}
