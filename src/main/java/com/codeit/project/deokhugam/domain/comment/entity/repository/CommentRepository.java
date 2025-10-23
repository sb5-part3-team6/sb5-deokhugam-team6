package com.codeit.project.deokhugam.domain.comment.entity.repository;

import com.codeit.project.deokhugam.domain.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
  List<Comment> findByReviewId(Long reviewId);

}
