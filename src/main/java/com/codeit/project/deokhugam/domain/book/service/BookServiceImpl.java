package com.codeit.project.deokhugam.domain.book.service;

import com.codeit.project.deokhugam.domain.book.dto.BookCreateRequest;
import com.codeit.project.deokhugam.domain.book.dto.BookDto;
import com.codeit.project.deokhugam.domain.book.dto.BookUpdateRequest;
import com.codeit.project.deokhugam.domain.book.entity.Book;
import com.codeit.project.deokhugam.domain.book.mapper.BookMapper;
import com.codeit.project.deokhugam.domain.book.repository.BookRepository;
import com.codeit.project.deokhugam.domain.book.storage.FileStorage;
import com.codeit.project.deokhugam.domain.review.repository.ReviewRepository;
import java.time.LocalDate;
import java.util.NoSuchElementException;
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
  private final ReviewRepository reviewRepository;

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
    //리뷰갯수 평점 넣어야 함
    Book book =new Book(
        bookData.title(),
        bookData.author(),
        bookData.description(),
        bookData.publisher(),
        bookData.publishedDate(),
        bookData.isbn(),
        thumbnailImageUrl);

    Book created = bookRepository.save(book);
    int reviewCount = getReviewCount(book.getId());
    double rating = getAverageRating(book.getId());
    return bookMapper.toDto(created, reviewCount, rating);
  }

  @Transactional(readOnly = true)
  @Override
  public BookDto update(Long bookId, BookUpdateRequest bookData, MultipartFile thumbnailImage) {
    Book book = bookRepository.findById(bookId)
        .orElseThrow(()-> new NoSuchElementException("도서가 존재하지 않습니다."));

    String newThumbnailImageUrl = book.getThumbnailUrl();
    if(thumbnailImage!=null && !thumbnailImage.isEmpty()){
      fileStorage.saveThumbnailImage(book.getIsbn(), thumbnailImage);
      newThumbnailImageUrl = fileStorage.getThumbnailImage(book.getIsbn());
    }

    String newTitle = bookData.title();
    String newAuthor = bookData.author();
    String newDescription = bookData.description();
    String newPublisher = bookData.publisher();
    LocalDate newpublishedDate = bookData.publishedDate();

    book.update(newTitle,newAuthor,newDescription,newPublisher,newpublishedDate,newThumbnailImageUrl);
    Book updated = bookRepository.save(book);
    int reviewCount = getReviewCount(updated.getId());
    double rating = getAverageRating(updated.getId());
    return bookMapper.toDto(updated,reviewCount,rating);
  }

  //리뷰 가져오기 (?바로 레포지토리로 가져와도 되나?)
  public double getAverageRating(Long bookId){
    double avg = reviewRepository.getAverageRating(bookId);
    return Math.round(avg * 10) / 10.0;
  }
  public int getReviewCount(Long bookId) {
    return reviewRepository.getReviewCount(bookId);
  }

  @Transactional(readOnly = true)
  @Override
  public BookDto findById(Long bookId) {
    Book book = bookRepository.findById(bookId)
        .orElseThrow(()->new NoSuchElementException("도서가 존재하지 않습니다."));
    int reviewCount = getReviewCount(book.getId());
    double rating = getAverageRating(book.getId());
    return bookMapper.toDto(book,reviewCount,rating);
  }

  @Transactional
  @Override
  public void softDelete(Long bookId) {
    Book book = bookRepository.findById(bookId)
        .orElseThrow(()-> new NoSuchElementException("도서가 존재하지 않습니다."));
    book.softDelete();
  }

  @Transactional
  @Override
  public void hardDelete(Long bookId) {
    Book book = bookRepository.findById(bookId)
        .orElseThrow(()-> new NoSuchElementException("도서가 존재하지 않습니다."));
    fileStorage.deleteThumbnailImage(book.getIsbn());
    bookRepository.deleteById(bookId);
  }
}
