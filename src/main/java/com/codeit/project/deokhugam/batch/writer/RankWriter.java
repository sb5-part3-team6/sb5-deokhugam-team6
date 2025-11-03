package com.codeit.project.deokhugam.batch.writer;

import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.entity.RankTarget;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import com.codeit.project.deokhugam.domain.rank.repository.RankRepository;
import com.codeit.project.deokhugam.domain.review.dto.event.ReviewRankedDeleteEvent;
import com.codeit.project.deokhugam.domain.review.dto.event.ReviewRankedEvent;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@StepScope
@RequiredArgsConstructor
public class RankWriter implements ItemWriter<Rank> {

  private final RankRepository rankRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public void write(Chunk<? extends Rank> chunk) {

    handlePreviousDailyRankAndDelete(chunk);

    AtomicInteger counter = new AtomicInteger(1);

    List<Rank> sorted = chunk.getItems()
                             .stream()
                             .sorted(Comparator.comparing(Rank::getScore)
                                               .reversed())
                             .peek(rank -> rank.updateRankNo(counter.getAndIncrement()))
                             .collect(Collectors.toList());

    rankRepository.saveAll(sorted);

    publishNewDailyRankNotifications(sorted);
  }

  private void handlePreviousDailyRankAndDelete(Chunk<? extends Rank> chunk) {
    if (isDailyReviewRank(chunk)) {
      LocalDate today = LocalDate.now();

      eventPublisher.publishEvent(new ReviewRankedDeleteEvent());

      rankRepository.deleteByDateAndTypeDaily(today);
    }
  }

  private void publishNewDailyRankNotifications(List<Rank> ranks) {
    ranks.stream()
         .filter(rank -> RankType.DAILY.name()
                                       .equals(rank.getType()))
         .filter(rank -> RankTarget.REVIEW.name()
                                          .equals(rank.getTarget()))
         .filter(rank -> rank.getRankNo() <= 10)
         .forEach(rank -> eventPublisher.publishEvent(new ReviewRankedEvent(rank.getTargetId())));
  }

  private boolean isDailyReviewRank(Chunk<? extends Rank> chunk) {
    return chunk.getItems()
                .stream()
                .anyMatch(rank -> RankType.DAILY.name()
                                                .equals(rank.getType()) && RankTarget.REVIEW.name()
                                                                                            .equals(
                                                                                                rank.getTarget()));
  }
}