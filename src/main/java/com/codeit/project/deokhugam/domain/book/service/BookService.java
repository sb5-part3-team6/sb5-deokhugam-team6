package com.codeit.project.deokhugam.domain.book.service;

import com.codeit.project.deokhugam.domain.book.dto.request.BookCreateRequest;
import com.codeit.project.deokhugam.domain.book.dto.response.BookDto;
import com.codeit.project.deokhugam.domain.book.dto.request.BookPopularRequest;
import com.codeit.project.deokhugam.domain.book.dto.request.BookSearchRequest;
import com.codeit.project.deokhugam.domain.book.dto.request.BookUpdateRequest;
import com.codeit.project.deokhugam.domain.book.dto.response.BookResponse;
import com.codeit.project.deokhugam.domain.book.dto.response.CursorPageResponseBookDto;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface BookService {
  BookDto create(BookCreateRequest bookData, MultipartFile thumbnailImage);
  BookDto update(Long bookId, BookUpdateRequest bookData, MultipartFile thumbnailImage);
  void softDelete(Long bookId);
  void hardDelete(Long bookId);
  BookDto findById(Long bookId);
  CursorPageResponseBookDto<BookDto> search(BookSearchRequest bookSearchReq);
  PageResponse popularList(BookPopularRequest request);
  BookResponse saveNaverThumbnail(String isbn);
}
