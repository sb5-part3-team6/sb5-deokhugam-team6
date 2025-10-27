package com.codeit.project.deokhugam.domain.comment.entity;

import com.codeit.project.deokhugam.domain.review.entity.Review;
import com.codeit.project.deokhugam.domain.user.entity.User;
import com.codeit.project.deokhugam.global.entity.BaseDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment extends BaseDeletableEntity {

  @ManyToOne
  @JoinColumn(name = "review_id", nullable = false)
  private Review review;
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
  @Column(length = 1024)
  private String content;

  public void updateContent(String content) {
    this.content = content;
  }
}