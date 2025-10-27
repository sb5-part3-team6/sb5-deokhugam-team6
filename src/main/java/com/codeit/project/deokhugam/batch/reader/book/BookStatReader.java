package com.codeit.project.deokhugam.batch.reader.book;

import com.codeit.project.deokhugam.domain.book.dto.BookStatDto;
import com.codeit.project.deokhugam.domain.book.repository.BookRepository;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
@StepScope
@RequiredArgsConstructor
public class BookStatReader implements ItemReader<BookStatDto> {

  private BookRepository bookRepository;
  private RankType rankType;
  private Iterator<BookStatDto> iterator;

  public BookStatReader(BookRepository bookRepository, RankType rankType) {
    this.bookRepository = bookRepository;
    this.rankType = rankType;
  }

  @Override
  public BookStatDto read() {
    if (iterator == null) {
      switch (rankType) {
        case DAILY -> iterator = bookRepository.getDailyStats()
                                               .iterator();
        case WEEKLY -> iterator = bookRepository.getWeeklyStats()
                                                .iterator();
        case MONTHLY -> iterator = bookRepository.getMonthlyStats()
                                                 .iterator();
        default -> throw new IllegalArgumentException("Unsupported RankType: " + rankType);
      }
    }
    return iterator.hasNext() ? iterator.next() : null;
  }
}