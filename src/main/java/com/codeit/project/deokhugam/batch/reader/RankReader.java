package com.codeit.project.deokhugam.batch.reader;

import com.codeit.project.deokhugam.domain.book.repository.BookRepository;
import com.codeit.project.deokhugam.domain.rank.entity.RankTarget;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import com.codeit.project.deokhugam.domain.review.repository.ReviewStatRepository;
import com.codeit.project.deokhugam.domain.user.repository.UserRepository;
import java.util.Iterator;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class RankReader implements ItemReader<Object> {

  private final BookRepository bookRepository;
  private final ReviewStatRepository reviewRepository;
  private final UserRepository userRepository;

  private final RankType rankType;
  private final RankTarget rankTarget;

  private Iterator<?> iterator;

  public RankReader(
      BookRepository bookRepository,
      ReviewStatRepository reviewRepository,
      UserRepository userRepository,
      @Value("#{jobParameters['rankType']}") String rankType,
      @Value("#{jobParameters['rankTarget']}") String rankTarget
  ) {
    this.bookRepository = bookRepository;
    this.reviewRepository = reviewRepository;
    this.userRepository = userRepository;
    this.rankType = RankType.valueOf(rankType);
    this.rankTarget = RankTarget.valueOf(rankTarget);
  }

  @Override
  public Object read() {
    if (iterator == null) {
      switch (rankTarget) {
        case BOOK -> iterator = switch (rankType) {
          case DAILY -> bookRepository.getStatsByPeriod(RankType.DAILY)
                                      .iterator();
          case WEEKLY -> bookRepository.getStatsByPeriod(RankType.WEEKLY)
                                       .iterator();
          case MONTHLY -> bookRepository.getStatsByPeriod(RankType.MONTHLY)
                                        .iterator();
          case ALL_TIME -> bookRepository.getStatsByPeriod(RankType.ALL_TIME)
                                         .iterator();
        };
        case REVIEW -> iterator = switch (rankType) {
          case DAILY -> reviewRepository.getStatsByPeriod(RankType.DAILY)
                                        .iterator();
          case WEEKLY -> reviewRepository.getStatsByPeriod(RankType.WEEKLY)
                                         .iterator();
          case MONTHLY -> reviewRepository.getStatsByPeriod(RankType.MONTHLY)
                                          .iterator();
          case ALL_TIME -> reviewRepository.getStatsByPeriod(RankType.ALL_TIME)
                                           .iterator();
        };
        case USER -> iterator = switch (rankType) {
          case DAILY -> userRepository.getStatsByPeriod(RankType.DAILY)
                                      .iterator();
          case WEEKLY -> userRepository.getStatsByPeriod(RankType.WEEKLY)
                                       .iterator();
          case MONTHLY -> userRepository.getStatsByPeriod(RankType.MONTHLY)
                                        .iterator();
          case ALL_TIME -> userRepository.getStatsByPeriod(RankType.ALL_TIME)
                                         .iterator();
        };
        default -> throw new IllegalArgumentException("Unsupported RankTarget: " + rankTarget);
      }
    }
    return iterator.hasNext() ? iterator.next() : null;
  }
}