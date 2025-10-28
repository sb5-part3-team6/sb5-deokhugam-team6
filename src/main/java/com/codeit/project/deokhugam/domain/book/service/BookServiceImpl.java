package com.codeit.project.deokhugam.domain.book.service;

import com.codeit.project.deokhugam.domain.book.dto.*;
import com.codeit.project.deokhugam.domain.book.entity.Book;
import com.codeit.project.deokhugam.domain.book.exception.BookNotFoundException;
import com.codeit.project.deokhugam.domain.book.mapper.BookMapper;
import com.codeit.project.deokhugam.domain.book.repository.BookRepository;
import com.codeit.project.deokhugam.domain.book.storage.FileStorage;
import com.codeit.project.deokhugam.domain.comment.entity.Comment;
import com.codeit.project.deokhugam.domain.comment.repository.CommentRepository;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.repository.RankRepository;
import com.codeit.project.deokhugam.domain.review.entity.Review;
import com.codeit.project.deokhugam.domain.review.repository.ReviewRepository;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  private final BookMapper bookMapper;
  private final BookRepository bookRepository;
  private final FileStorage fileStorage;
  private final ReviewRepository reviewRepository;
  private final CommentRepository commentRepository;
  private final RankRepository rankRepository;

  @Override @Transactional
  public BookDto create(BookCreateRequest bookData, MultipartFile thumbnailImage) {
    if (bookRepository.existsByisbn(bookData.isbn())) {
      throw new IllegalArgumentException("바코드가 중복되어 요청하신 ISBN을 사용할 수 없습니다. 다른 ISBN을 사용해주세요.");
    }

    String thumbnailImageUrl = null;
    if (thumbnailImage != null && !thumbnailImage.isEmpty()) {
      fileStorage.saveThumbnailImage(bookData.isbn(), thumbnailImage);
      thumbnailImageUrl = fileStorage.getThumbnailImage(bookData.isbn());
    }
    //리뷰갯수 평점 넣어야 함
    Book book = new Book(
        bookData.title(),
        bookData.author(),
        bookData.description(),
        bookData.publisher(),
        bookData.publishedDate(),
        bookData.isbn(),
        thumbnailImageUrl);

    Book created = bookRepository.save(book);
    long reviewCount = getReviewCount(book.getId());
    double rating = getAverageRating(book.getId());
    return bookMapper.toDto(created, reviewCount, rating);
  }

  @Override @Transactional
  public BookDto update(Long bookId, BookUpdateRequest bookData, MultipartFile thumbnailImage) {
    Book book = bookRepository.findById(bookId)
                              .orElseThrow(() -> new NoSuchElementException("도서가 존재하지 않습니다."));

    String newThumbnailImageUrl = book.getThumbnailUrl();
    if (thumbnailImage != null && !thumbnailImage.isEmpty()) {
      fileStorage.saveThumbnailImage(book.getIsbn(), thumbnailImage);
      newThumbnailImageUrl = fileStorage.getThumbnailImage(book.getIsbn());
    }

    String newTitle = bookData.title();
    String newAuthor = bookData.author();
    String newDescription = bookData.description();
    String newPublisher = bookData.publisher();
    LocalDate newpublishedDate = bookData.publishedDate();

    book.update(newTitle, newAuthor, newDescription, newPublisher, newpublishedDate,
        newThumbnailImageUrl);
    Book updated = bookRepository.save(book);
    long reviewCount = getReviewCount(updated.getId());
    double rating = getAverageRating(updated.getId());
    return bookMapper.toDto(updated, reviewCount, rating);
  }

  @Override @Transactional(readOnly = true)
  public BookDto findById(Long bookId) {
    Book book = verifyBookExists(bookId);
    long reviewCount = getReviewCount(book.getId());
    double rating = getAverageRating(book.getId());
    return bookMapper.toDto(book, reviewCount, rating);
  }

  @Override @Transactional(readOnly = true)
  public CursorPageResponseBookDto<BookDto> search(BookSearchRequest bookSearchReq) {
    int pageSize = bookSearchReq.limit() == null ? 50 : bookSearchReq.limit();
    List<BookDto> bookList = bookRepository.findBooks(bookSearchReq, pageSize + 1);

    boolean hasNext = bookList.size() > pageSize;
    bookList = hasNext ? bookList.subList(0, pageSize) : bookList;

    String nextCursor = null;
    String nextAfter = null;

    if (hasNext && !bookList.isEmpty()) {
      BookDto last = bookList.get(bookList.size() - 1);

      switch (bookSearchReq.orderBy()) {
        case "title" -> nextCursor = last.title();
        case "publishedDate" -> nextCursor = last.publishedDate()
                                                 .toString();
        case "rating" -> nextCursor = String.valueOf(last.rating());
        case "reviewCount" -> nextCursor = String.valueOf(last.reviewCount());
        default -> nextCursor = last.title();
      }
      nextAfter = last.createdAt()
                      .toString();
    }
    return new CursorPageResponseBookDto<>(
        bookList,
        nextCursor,
        nextAfter,
        pageSize,
        bookList.size(),
        String.valueOf(hasNext)
    );
  }

  @Override
  public PageResponse popularList(BookPopularRequest request) {

    List<Rank> rankList = bookRepository.findRanksByType(request.period(), request.direction(), request.limit());

    Long total = rankRepository.countAllByTypeForBook(request.period());
    boolean hasNext = rankList.size() > request.limit();
    String nextCursor = null;
    String nextAfter = null;

    if(hasNext) {
      Rank lastItem = rankList.get(rankList.size() - 1);
      nextCursor = lastItem.getId().toString();
      nextAfter = lastItem.getCreatedAt().toString();
      rankList.remove(lastItem);
    }

    List<PopularBookDto> content = rankList.stream().map(rank -> {
      Book book = verifyBookExists(rank.getTargetId());
      Long reviewCount = getReviewCount(book.getId());
      Double rating = getAverageRating(book.getId());
      return PopularBookDto.builder()
              .id(rank.getId())
              .bookId(book.getId())
              .title(book.getTitle())
              .author(book.getAuthor())
              .thumbnailUrl(book.getThumbnailUrl())
              .period(request.period())
              .rank(rank.getRankNo())
              .score(rank.getScore())
              .reviewCount(reviewCount)
              .rating(rating)
              .createdAt(rank.getCreatedAt())
              .build();
    }).toList();

    return PageResponse.builder()
            .content(content)
            .nextCursor(nextCursor)
            .size(rankList.size())
            .totalElements(total)
            .hasNext(hasNext)
            .nextAfter(nextAfter)
            .build();
  }

  @Override @Transactional
  public void softDelete(Long bookId) {
    Book book = bookRepository.findById(bookId)
                              .orElseThrow(() -> new NoSuchElementException("도서가 존재하지 않습니다."));
    book.softDelete();
    List<Review> reviews = reviewRepository.findByBookId(bookId);
    reviews.forEach(review -> {
      review.softDelete();
      List<Comment> comments = commentRepository.findByReviewId(review.getId());
      comments.forEach(Comment::softDelete);
    });
    // TODO Review, Comment 소프트 삭제 고려
  }

  @Override @Transactional
  public void hardDelete(Long bookId) {
    Book book = bookRepository.findById(bookId)
                              .orElseThrow(() -> new NoSuchElementException("도서가 존재하지 않습니다."));

    List<Long> reviewIds = reviewRepository.findIdsByBookId(bookId);
    if (!reviewIds.isEmpty()) {
      commentRepository.bulkDeleteByReviewIds(reviewIds);
    }

    reviewRepository.bulkDeleteByBookId(bookId);

    fileStorage.deleteThumbnailImage(book.getIsbn());
    bookRepository.deleteById(bookId);
    // TODO Review, Comment 삭제 고려
  }

  private double getAverageRating(Long bookId) {
    double avg = reviewRepository.getAverageRating(bookId);
    return Math.round(avg * 10) / 10.0;
  }

  private Long getReviewCount(Long bookId) {
    return reviewRepository.getReviewCount(bookId);
  }

  private Book verifyBookExists(Long bookId) {
    return bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
  }
}
