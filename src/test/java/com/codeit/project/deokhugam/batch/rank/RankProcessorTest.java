package com.codeit.project.deokhugam.batch.rank;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.codeit.project.deokhugam.batch.processor.RankProcessor;
import com.codeit.project.deokhugam.domain.book.dto.BookStatDto;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.entity.RankTarget;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import com.codeit.project.deokhugam.domain.review.dto.ReviewStatDto;
import com.codeit.project.deokhugam.domain.user.dto.UserStatDto;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class RankProcessorTest {

  @Test
  void process_BookTarget() {
    RankProcessor processor = new RankProcessor(RankType.DAILY.name(), RankTarget.BOOK.name());
    BookStatDto book = new BookStatDto(1L, 5L, 4.5);

    Rank result = processor.process(book);

    assertThat(result.getTarget()).isEqualTo(RankTarget.BOOK.name());
    assertThat(result.getTargetId()).isEqualTo(1L);
    assertThat(result.getScore()).isEqualTo(BigDecimal.valueOf(5L * 0.4 + 4.5 * 0.6));
  }

  @Test
  void process_ReviewTarget() {
    RankProcessor processor = new RankProcessor(RankType.WEEKLY.name(), RankTarget.REVIEW.name());
    ReviewStatDto review = new ReviewStatDto(10L, 3L, 7L);

    Rank result = processor.process(review);

    assertThat(result.getTarget()).isEqualTo(RankTarget.REVIEW.name());
    assertThat(result.getTargetId()).isEqualTo(10L);
    assertThat(result.getScore()).isEqualTo(BigDecimal.valueOf(3L * 0.3 + 7L * 0.7));
  }

  @Test
  void process_UserTarget() {
    RankProcessor processor = new RankProcessor(RankType.ALL_TIME.name(), RankTarget.USER.name());
    UserStatDto user = new UserStatDto(2L, 80L, 5L, BigDecimal.TEN);

    Rank result = processor.process(user);

    assertThat(result.getTarget()).isEqualTo(RankTarget.USER.name());
    assertThat(result.getTargetId()).isEqualTo(2L);
    assertThat(result.getScore()).isEqualTo(BigDecimal.valueOf(80L * 0.2 + 5L * 0.3 + BigDecimal.TEN.doubleValue() * 0.5));
  }
}