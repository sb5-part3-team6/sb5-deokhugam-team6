package com.codeit.project.deokhugam.batch.rank;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;

import com.codeit.project.deokhugam.batch.writer.RankWriter;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.repository.RankRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.Chunk;

@ExtendWith(MockitoExtension.class)
class RankWriterTest {

  @Mock
  RankRepository repository;

  @Test
  @DisplayName("점수 기준 내림차순 정렬 및 순위 부여 확인")
  void writeTest() {

    RankWriter writer = new RankWriter(repository);

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
  
  // TODO 추가 로직에 대한 테스트
}