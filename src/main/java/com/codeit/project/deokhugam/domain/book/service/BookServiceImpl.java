package com.codeit.project.deokhugam.domain.book.service;

import com.codeit.project.deokhugam.domain.book.dto.BookCreateRequest;
import com.codeit.project.deokhugam.domain.book.dto.BookDto;
import org.springframework.web.multipart.MultipartFile;

public class BookServiceImpl implements BookService {

  @Override
  public BookDto create(BookCreateRequest bookData, MultipartFile thumbnailImage) {

    return null;
  }
}
