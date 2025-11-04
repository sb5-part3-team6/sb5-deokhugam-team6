package com.codeit.project.deokhugam.batch.rank;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import com.codeit.project.deokhugam.batch.writer.RankWriter;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.entity.RankTarget;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import com.codeit.project.deokhugam.domain.rank.repository.RankRepository;
import com.codeit.project.deokhugam.domain.review.dto.event.ReviewRankedDeleteEvent;
import com.codeit.project.deokhugam.domain.review.dto.event.ReviewRankedEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.Chunk;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class RankWriterTest {

  @Mock
  RankRepository repository;

  @Mock
  ApplicationEventPublisher eventPublisher;

  RankWriter writer;

  @BeforeEach
  void setUp() {
    writer = new RankWriter(repository, eventPublisher);
  }

  @Test
  @DisplayName("점수 기준 내림차순 정렬 및 순위 부여 확인")
  void writeTest() {

    RankWriter writer = new RankWriter(repository, eventPublisher);

    Rank low = new Rank("BOOK", 1L, "DAILY", null, BigDecimal.valueOf(10));
    Rank high = new Rank("BOOK", 2L, "DAILY", null, BigDecimal.valueOf(20));

    writer.write(Chunk.of(low, high));

    ArgumentCaptor<List<Rank>> captor = ArgumentCaptor.forClass(List.class);
    verify(repository).saveAll(captor.capture());

    List<Rank> saved = captor.getValue();

    assertThat(saved).hasSize(2);
    assertThat(saved.get(0)
                    .getScore()).isEqualTo(BigDecimal.valueOf(20));
    assertThat(saved.get(0)
                    .getRankNo()).isEqualTo(1);
    assertThat(saved.get(1)
                    .getRankNo()).isEqualTo(2);
  }

  @Test
  @DisplayName("Daily Review 랭킹이 존재하면 삭제 이벤트 발행 및 삭제 호출")
  void write_DailyReviewRank_DeleteEvent() {
    Rank dailyReview = new Rank(RankTarget.REVIEW.name(), 1L, RankType.DAILY.name(), null,
        BigDecimal.TEN);

    writer.write(Chunk.of(dailyReview));

    verify(eventPublisher).publishEvent(any(ReviewRankedDeleteEvent.class));
    verify(repository).deleteByDateAndTypeDaily(any());
  }

  @Test
  @DisplayName("Daily Review 10위권 랭킹 이벤트 발행 확인")
  void write_DailyReviewRank_NotificationEvent() {

    List<Rank> ranks = new ArrayList<>();

    for (int i = 0; i < 11; i++) {
      ranks.add(new Rank(RankTarget.REVIEW.name(), i + 1L, RankType.DAILY.name(), null,
          BigDecimal.valueOf(100 - i)));
    }

    writer.write(Chunk.of(ranks.toArray(Rank[]::new)));

    ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
    verify(eventPublisher, atLeastOnce()).publishEvent(captor.capture());

    List<Object> events = captor.getAllValues();

    boolean hasRank1Event = events.stream()
                                  .anyMatch(e ->
                                      e instanceof ReviewRankedEvent &&
                                      ((ReviewRankedEvent) e).reviewId()
                                                             .equals(1L)
                                  );

    boolean hasRank11Event = events.stream()
                                   .anyMatch(e ->
                                       e instanceof ReviewRankedEvent &&
                                       ((ReviewRankedEvent) e).reviewId()
                                                              .equals(11L)
                                   );

    assertThat(hasRank1Event).isTrue();
    assertThat(hasRank11Event).isFalse();
  }
}