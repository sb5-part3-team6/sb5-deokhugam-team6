package com.codeit.project.deokhugam.domain.comment.repository;

import com.codeit.project.deokhugam.domain.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

  List<Comment> findByReviewId(Long reviewId);

  @Modifying
  @Query("DELETE FROM Comment c WHERE c.review.id IN :reviewIds")
  void bulkDeleteByReviewIds(List<Long> reviewId);

  Integer countByReviewId(Long reviewId);

  Integer countByReviewIdAndDeletedAtIsNull(Long reviewId);

  @Transactional @Modifying
  void deleteAllByReviewId(Long reviewId);
}
