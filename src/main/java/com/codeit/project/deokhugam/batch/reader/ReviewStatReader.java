package com.codeit.project.deokhugam.batch.reader;

import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import com.codeit.project.deokhugam.domain.review.dto.ReviewStatDto;
import com.codeit.project.deokhugam.domain.review.repository.ReviewStatRepository;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
@StepScope
@RequiredArgsConstructor
public class ReviewStatReader implements ItemReader<ReviewStatDto> {

  private ReviewStatRepository reviewStatRepository;
  private RankType rankType;
  private Iterator<ReviewStatDto> iterator;

  public ReviewStatReader(ReviewStatRepository reviewStatRepository, RankType rankType) {
    this.reviewStatRepository = reviewStatRepository;
    this.rankType = rankType;
  }

  @Override
  public ReviewStatDto read() {
    if (iterator == null) {
      switch (rankType) {
        case DAILY -> iterator = reviewStatRepository.getStatsByPeriod(RankType.DAILY)
                                                     .iterator();
        case WEEKLY -> iterator = reviewStatRepository.getStatsByPeriod(RankType.WEEKLY)
                                                      .iterator();
        case MONTHLY -> iterator = reviewStatRepository.getStatsByPeriod(RankType.MONTHLY)
                                                       .iterator();
        case ALL_TIME -> iterator = reviewStatRepository.getStatsByPeriod(RankType.ALL_TIME)
                                                        .iterator();
        default -> throw new IllegalArgumentException("Unsupported RankType: " + rankType);
      }
    }
    return iterator.hasNext() ? iterator.next() : null;
  }
}