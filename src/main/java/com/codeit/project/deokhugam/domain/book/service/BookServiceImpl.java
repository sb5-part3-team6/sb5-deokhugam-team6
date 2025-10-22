package com.codeit.project.deokhugam.domain.book.service;

import com.codeit.project.deokhugam.domain.book.dto.BookCreateRequest;
import com.codeit.project.deokhugam.domain.book.dto.BookDto;
import com.codeit.project.deokhugam.domain.book.entity.Book;
import com.codeit.project.deokhugam.domain.book.mapper.BookMapper;
import com.codeit.project.deokhugam.domain.book.repository.BookRepository;
import com.codeit.project.deokhugam.domain.book.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
  private final BookMapper bookMapper;
  private final BookRepository bookRepository;
  private final FileStorage fileStorage;

  @Override
  @Transactional
  public BookDto create(BookCreateRequest bookData, MultipartFile thumbnailImage) {
    if(bookRepository.existsByisbn(bookData.isbn())){
      throw new IllegalArgumentException("바코드가 중복되어 요청하신 ISBN을 사용할 수 없습니다. 다른 ISBN을 사용해주세요.");
    }

    String thumbnailImageUrl = null;
    if(thumbnailImage!=null && !thumbnailImage.isEmpty()){
      fileStorage.saveThumbnailImage(bookData.isbn(), thumbnailImage);
      thumbnailImageUrl = fileStorage.getThumbnailImage(bookData.isbn());
    }
    Book book =new Book(
        bookData.title(),
        bookData.author(),
        bookData.description(),
        bookData.publisher(),
        bookData.publishedDate(),
        bookData.isbn(),
        thumbnailImageUrl);

    Book created = bookRepository.save(book);
    return bookMapper.toDto(created);
  }
}
