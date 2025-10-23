package com.codeit.project.deokhugam.domain.comment.repository;

import com.codeit.project.deokhugam.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    Integer countByReviewId(Long reviewId);
    void deleteAllByReviewId(Long reviewId);
}
