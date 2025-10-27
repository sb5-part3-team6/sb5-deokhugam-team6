package com.codeit.project.deokhugam.domain.book.repository;

import com.codeit.project.deokhugam.domain.book.dto.BookDto;
import com.codeit.project.deokhugam.domain.book.dto.BookSearchRequest;
import com.codeit.project.deokhugam.domain.book.dto.BookStatDto;
import java.util.List;

public interface BookRepositoryCustom {
  List<BookDto> findBooks(BookSearchRequest bookSearchReq, int pageSize);
  List<BookStatDto> getDailyStats();
  List<BookStatDto> getWeeklyStats();
  List<BookStatDto> getMonthlyStats();
}
