package com.codeit.project.deokhugam.domain.book.repository;

import com.codeit.project.deokhugam.domain.book.dto.BookDto;
import com.codeit.project.deokhugam.domain.book.dto.BookSearchRequest;
import java.util.List;

public interface BookRepositoryCustom {
  List<BookDto> findBooks(BookSearchRequest bookSearchReq, int pageSize);
}
