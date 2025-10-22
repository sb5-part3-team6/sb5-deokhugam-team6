package com.codeit.project.deokhugam.domain.book.service;

import com.codeit.project.deokhugam.domain.book.dto.BookCreateRequest;
import com.codeit.project.deokhugam.domain.book.dto.BookDto;
import com.codeit.project.deokhugam.domain.book.dto.BookUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

public interface BookService {
  BookDto create(BookCreateRequest bookData, MultipartFile thumbnailImage);
  BookDto update(Long bookId, BookUpdateRequest bookData, MultipartFile thumbnailImage);
  void softDelete(Long bookId);
  void hardDelete(Long bookId);
  BookDto findById(Long bookId);

}
