package com.codeit.project.deokhugam.batch.processor;

import com.codeit.project.deokhugam.domain.book.dto.BookStatDto;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.entity.RankTarget;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

@RequiredArgsConstructor
public class BookStatProcessor implements ItemProcessor<BookStatDto, Rank> {

  private final RankType rankType;
  private final RankTarget rankTarget;

  @Override
  public Rank process(BookStatDto dto) {
    double score = (dto.reviewCount() * 0.4) + (dto.avgRating() * 0.6);

    return new Rank(
        rankTarget.name(),
        dto.bookId(),
        rankType.name(),
        null,
        BigDecimal.valueOf(score)
    );
  }
}