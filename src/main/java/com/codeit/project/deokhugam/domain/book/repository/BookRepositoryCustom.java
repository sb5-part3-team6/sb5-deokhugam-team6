package com.codeit.project.deokhugam.domain.book.repository;

import com.codeit.project.deokhugam.domain.book.dto.response.BookDto;
import com.codeit.project.deokhugam.domain.book.dto.request.BookSearchRequest;
import com.codeit.project.deokhugam.domain.book.dto.BookStatDto;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import java.util.List;

public interface BookRepositoryCustom {
  List<BookDto> findBooks(BookSearchRequest bookSearchReq, int pageSize);
  List<BookStatDto> getStatsByPeriod(RankType type);
  List<Rank> findRanksByType(String type, String direction, int limit);
}
