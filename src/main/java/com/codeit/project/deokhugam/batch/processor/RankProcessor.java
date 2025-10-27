package com.codeit.project.deokhugam.batch.processor;

import com.codeit.project.deokhugam.domain.book.dto.BookStatDto;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.entity.RankTarget;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import com.codeit.project.deokhugam.domain.review.dto.ReviewStatDto;
import com.codeit.project.deokhugam.domain.user.dto.UserStatDto;
import java.math.BigDecimal;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class RankProcessor implements ItemProcessor<Object, Rank> {

  private final RankType rankType;
  private final RankTarget rankTarget;

  public RankProcessor(@Value("#{jobParameters['rankType']}") String rankType,
      @Value("#{jobParameters['rankTarget']}") String rankTarget) {
    this.rankType = RankType.valueOf(rankType);
    this.rankTarget = RankTarget.valueOf(rankTarget);
  }

  @Override
  public Rank process(Object dto) {
    double score;
    long id;

    switch (rankTarget) {
      case BOOK -> {
        BookStatDto book = (BookStatDto) dto;
        score = book.reviewCount() * 0.4 + book.avgRating() * 0.6;
        id = book.bookId();
      }
      case REVIEW -> {
        ReviewStatDto review = (ReviewStatDto) dto;
        score = review.likeCount() * 0.3 + review.commentCount() * 0.7;
        id = review.reviewId();
      }
      case USER -> {
        UserStatDto user = (UserStatDto) dto;
        score = user.reviewRankScore() * 0.5 + user.likeCount() * 0.2 + user.commentCount() * 0.3;
        id = user.userId();
      }
      default -> throw new IllegalArgumentException("Unsupported RankTarget: " + rankTarget);
    }

    return new Rank(rankTarget.name(), id, rankType.name(), null, BigDecimal.valueOf(score));
  }
}