package com.codeit.project.deokhugam.domain.rank.entity;

import com.codeit.project.deokhugam.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ranks")
public class Rank extends BaseEntity {

  @Column(length = 10, nullable = false)
  private String target;

  private Long targetId;

  @Column(length = 10)
  private String type;

  private Integer rankNo;

  private BigDecimal score;

  public void updateRankNo(Integer rankNo) {
    this.rankNo = rankNo;
  }
}
