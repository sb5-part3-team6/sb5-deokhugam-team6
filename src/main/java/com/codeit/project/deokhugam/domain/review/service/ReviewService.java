package com.codeit.project.deokhugam.domain.review.service;

import com.codeit.project.deokhugam.domain.review.dto.request.ReviewCreateRequest;
import com.codeit.project.deokhugam.domain.review.dto.request.ReviewPopularQueryParams;
import com.codeit.project.deokhugam.domain.review.dto.request.ReviewQueryParams;
import com.codeit.project.deokhugam.domain.review.dto.request.ReviewUpdateRequest;
import com.codeit.project.deokhugam.domain.review.dto.response.ReviewDto;
import com.codeit.project.deokhugam.domain.review.dto.response.ReviewLikeDto;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;

public interface ReviewService {
    ReviewDto create(ReviewCreateRequest request);
    ReviewLikeDto createLike(Long reviewId, Long userId);
    PageResponse list(ReviewQueryParams params, Long userId);
    ReviewDto detail(Long reviewId, Long userId);
    PageResponse popularList(ReviewPopularQueryParams params);
    ReviewDto update(Long reviewId, Long userId, ReviewUpdateRequest request);
    void softDelete(Long reviewId, Long userId);
    void hardDelete(Long reviewId, Long userId);
}