package com.codeit.project.deokhugam.domain.review.repository;

import com.codeit.project.deokhugam.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
  @Query("""
  select coalesce(avg(r.rating),0.0)
  from Review r
  where :bookId = r.book.id
  and r.deletedAt IS NULL
""")
  double getAverageRating(@Param("bookId")Long bookId);

  @Query("""
  select coalesce(count(r),0)
  from Review r
  where :bookId = r.book.id
  and r.deletedAt IS NULL 
""")
  int getReviewCount(@Param("bookId")Long bookId);

}
