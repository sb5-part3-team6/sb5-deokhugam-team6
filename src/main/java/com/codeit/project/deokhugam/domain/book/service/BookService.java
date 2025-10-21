package com.codeit.project.deokhugam.domain.book.service;

import com.codeit.project.deokhugam.domain.book.dto.BookCreateRequest;
import com.codeit.project.deokhugam.domain.book.dto.BookDto;
import org.springframework.web.multipart.MultipartFile;

public interface BookService {
  BookDto create(BookCreateRequest bookData, MultipartFile thumbnailImage);

}
