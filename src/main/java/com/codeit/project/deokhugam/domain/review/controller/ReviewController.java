package com.codeit.project.deokhugam.domain.review.controller;

import com.codeit.project.deokhugam.domain.review.dto.*;
import com.codeit.project.deokhugam.domain.review.service.ReviewService;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDto> create(@RequestBody ReviewCreateRequest request) {
        ReviewDto response = reviewService.create(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{reviewId}/like")
    public ResponseEntity<ReviewLikeDto> createLike(@PathVariable Long reviewId, @RequestHeader("Deokhugam-Request-User-ID") Long userId) {
        ReviewLikeDto response = reviewService.createLike(reviewId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse> list(ReviewQueryParams params, @RequestHeader("Deokhugam-Request-User-ID") Long userId) {
        PageResponse response = reviewService.list(params, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> detail(@PathVariable Long reviewId, @RequestHeader("Deokhugam-Request-User-ID") Long userId) {
        ReviewDto response = reviewService.detail(reviewId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/popular")
    public ResponseEntity<PageResponse> popularList(ReviewPopularQueryParams params) {
        PageResponse response = reviewService.popularList(params);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> update(@PathVariable Long reviewId, @RequestHeader("Deokhugam-Request-User-ID") Long userId, @RequestBody ReviewUpdateRequest request) {
        ReviewDto response = reviewService.update(reviewId, userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> softDelete(@PathVariable Long reviewId, @RequestHeader("Deokhugam-Request-User-ID") Long userId) {
        reviewService.softDelete(reviewId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("리뷰 삭제 성공");
    }

    @DeleteMapping("/{reviewId}/hard")
    public ResponseEntity<String> hardDelete(@PathVariable Long reviewId, @RequestHeader("Deokhugam-Request-User-ID") Long userId) {
        reviewService.hardDelete(reviewId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("리뷰 삭제 성공");
    }

}
