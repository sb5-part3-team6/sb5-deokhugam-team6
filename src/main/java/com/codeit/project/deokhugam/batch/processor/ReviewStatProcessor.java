package com.codeit.project.deokhugam.batch.processor;

import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.entity.RankTarget;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import com.codeit.project.deokhugam.domain.review.dto.ReviewStatDto;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

@RequiredArgsConstructor
public class ReviewStatProcessor implements ItemProcessor<ReviewStatDto, Rank> {

  private final RankType rankType;
  private final RankTarget rankTarget;

  @Override
  public Rank process(ReviewStatDto dto) {
    double score = (dto.likeCount() * 0.3) + (dto.commentCount() * 0.7);

    return new Rank(
        rankTarget.name(),
        dto.reviewId(),
        rankType.name(),
        null,
        BigDecimal.valueOf(score)
    );
  }
}