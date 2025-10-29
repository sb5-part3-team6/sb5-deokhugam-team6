package com.codeit.project.deokhugam.domain.review.controller;

import com.codeit.project.deokhugam.domain.review.dto.request.ReviewCreateRequest;
import com.codeit.project.deokhugam.domain.review.dto.response.ReviewDto;
import com.codeit.project.deokhugam.domain.review.dto.response.ReviewLikeDto;
import com.codeit.project.deokhugam.domain.review.dto.request.ReviewPopularQueryParams;
import com.codeit.project.deokhugam.domain.review.dto.request.ReviewQueryParams;
import com.codeit.project.deokhugam.domain.review.dto.request.ReviewUpdateRequest;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface ReviewApi {


  ResponseEntity<ReviewDto> create(@RequestBody ReviewCreateRequest request);

  ResponseEntity<ReviewLikeDto> createLike(Long reviewId, Long userId);

  ResponseEntity<PageResponse> list(ReviewQueryParams params, Long userId);

  ResponseEntity<ReviewDto> detail(Long reviewId, Long userId);

  ResponseEntity<PageResponse> popularList(ReviewPopularQueryParams params);

  ResponseEntity<ReviewDto> update(Long reviewId, Long userId, ReviewUpdateRequest request);

  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "리뷰 삭제 성공")})
  ResponseEntity<String> softDelete(Long reviewId, Long userId);

  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "리뷰 삭제 성공")})
  ResponseEntity<String> hardDelete(Long reviewId, Long userId);
}
