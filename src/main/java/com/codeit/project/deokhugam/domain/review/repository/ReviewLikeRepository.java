package com.codeit.project.deokhugam.domain.review.repository;

import com.codeit.project.deokhugam.domain.review.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByReviewIdAndUserId(Long reviewId, Long userId);
    void deleteAllByReviewId(Long reviewId);
    Integer countByReviewId(Long reviewId);
    boolean existsByReviewIdAndUserId(Long reviewId, Long userId);
}
