package com.codeit.project.deokhugam.domain.review.repository;

import com.codeit.project.deokhugam.domain.review.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByReviewIdAndUserId(Long reviewId, Long userId);

    @Transactional @Modifying
    void deleteAllByReviewId(Long reviewId);

    Integer countAllByReviewIdAndDeletedAtIsNull(Long reviewId);

    Integer countAllByUserIdAndDeletedAtIsNull(Long userId);

    boolean existsByReviewIdAndUserId(Long reviewId, Long userId);
}
