package com.codeit.project.deokhugam.domain.book.repository;

import com.codeit.project.deokhugam.domain.book.dto.BookDto;
import com.codeit.project.deokhugam.domain.book.dto.BookSearchRequest;
import com.codeit.project.deokhugam.domain.book.entity.Book;
import java.util.List;

public interface BookQueryRepository {
  List<BookDto> findBooks(BookSearchRequest bookSearchReq, int pageSize);

}
