package com.codeit.project.deokhugam.domain.review.repository;

import com.codeit.project.deokhugam.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUserIdAndBookId(Long userId, Long bookId);

    @Query("SELECT COUNT(r.id) FROM Review r")
    Long countTotal();
}
