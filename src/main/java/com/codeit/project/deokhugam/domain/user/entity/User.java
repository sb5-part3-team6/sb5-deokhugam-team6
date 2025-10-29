package com.codeit.project.deokhugam.domain.user.entity;


import com.codeit.project.deokhugam.global.common.entity.BaseDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseDeletableEntity {

  @Column(length = 300, unique = true, nullable = false)
  private String email;
  @Column(length = 50, unique = true, nullable = false)
  private String nickname;
  @Column(length = 300, nullable = false)
  private String password;

  public void updateNickname(String nickname) {
    this.nickname = nickname;
  }
}
