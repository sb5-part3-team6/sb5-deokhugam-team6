package com.codeit.project.deokhugam.domain.review.entity;

import com.codeit.project.deokhugam.domain.user.entity.User;
import com.codeit.project.deokhugam.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review_likes",
    uniqueConstraints = @UniqueConstraint(columnNames = {"review_id", "user_id"}))
public class ReviewLike extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "review_id", nullable = false)
  private Review review;
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
