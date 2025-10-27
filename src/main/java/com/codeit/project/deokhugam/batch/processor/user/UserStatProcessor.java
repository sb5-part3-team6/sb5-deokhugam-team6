package com.codeit.project.deokhugam.batch.processor.user;

import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.entity.RankTarget;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import com.codeit.project.deokhugam.domain.user.dto.UserStatDto;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

@RequiredArgsConstructor
public class UserStatProcessor implements ItemProcessor<UserStatDto, Rank> {

  private final RankType rankType;
  private final RankTarget rankTarget;

  @Override
  public Rank process(UserStatDto dto) {

    double score =
        (dto.reviewRankScore() * 0.5) + (dto.likeCount() * 0.2) + (dto.commentCount() * 0.3);
    return new Rank(
        rankTarget.name(),
        dto.userId(),
        rankType.name(),
        null,
        BigDecimal.valueOf(score)
    );
  }
}