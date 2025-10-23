package com.codeit.project.deokhugam.domain.review.repository;

import com.codeit.project.deokhugam.domain.review.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  @Query("""
        select coalesce(avg(r.rating),0.0)
        from Review r
        where :bookId = r.book.id
        and r.deletedAt IS NULL
      """)
  double getAverageRating(Long bookId);

  @Query("""
        select coalesce(count(r),0)
        from Review r
        where :bookId = r.book.id
        and r.deletedAt IS NULL
      """)
  Long getReviewCount(Long bookId);

  List<Review> findByBookId(Long bookId);

  @Query("SELECT r.id FROM Review r WHERE r.book.id = :bookId")
  List<Long> findIdsByBookId(Long bookId);

  @Modifying
  @Query("DELETE FROM Review r WHERE r.book.id = :bookId")
  void bulkDeleteByBookId(Long bookId);

  boolean existsByUserIdAndBookId(Long userId, Long bookId);

  @Query("SELECT COUNT(r.id) FROM Review r")
  Long countTotal();
}
