package com.codeit.project.deokhugam.domain.book.repository.impl;

import com.codeit.project.deokhugam.domain.book.dto.BookDto;
import com.codeit.project.deokhugam.domain.book.dto.BookOrderBy;
import com.codeit.project.deokhugam.domain.book.dto.BookSearchRequest;
import com.codeit.project.deokhugam.domain.book.dto.Direction;
import com.codeit.project.deokhugam.domain.book.entity.QBook;
import com.codeit.project.deokhugam.domain.book.repository.BookQueryRepository;
import com.codeit.project.deokhugam.domain.review.entity.QReview;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookQueryRepositoryImpl implements BookQueryRepository {
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<BookDto> findBooks(BookSearchRequest bookSearchReq, int pageSize) {
    QBook book = QBook.book;
    QReview review = QReview.review;

    NumberExpression<Long> reviewCount = review.id.countDistinct().coalesce(0L);
    NumberExpression<Double> ratingAvg = review.rating.avg().coalesce(0.0);

    //검색 조건
    BooleanBuilder where = new BooleanBuilder();
    if(bookSearchReq.getKeyword()!=null && !bookSearchReq.getKeyword().isBlank()){
      String keyword = "%"+bookSearchReq.getKeyword()+"%";
      where.and(
        book.title.containsIgnoreCase(keyword)
            .or(book.author.likeIgnoreCase(keyword))
            .or(book.isbn.likeIgnoreCase(keyword))
          );
    }

    //정렬조건
    OrderSpecifier<?> primaryOrder = getPrimaryOrder(book,reviewCount, ratingAvg,bookSearchReq);
    OrderSpecifier<?> secondaryOrder = getSecondaryOrder(book, bookSearchReq);

    //커서
    if(bookSearchReq.getCursor() !=null && bookSearchReq.getAfter()!=null){
      where.and(buildCursorPredicate(book,reviewCount, ratingAvg, bookSearchReq));
    }



    return jpaQueryFactory
        .select(Projections.constructor(BookDto.class,
            book.id,
            book.title,
            book.author,
            book.description,
            book.publisher,
            book.publishedAt,
            book.isbn,
            book.thumbnailUrl,
            reviewCount,
            ratingAvg,
            book.createdAt,
            book.updatedAt
        ))
        .from(book)
        .leftJoin(review).on(review.book.eq(book))
        .where(where)
        .groupBy(book.id)
        .orderBy(primaryOrder, secondaryOrder)
        .limit(pageSize+1)
        .fetch();
  }
  private OrderSpecifier<?> getPrimaryOrder(QBook book,NumberExpression<Long> reviewCount, NumberExpression<Double> ratingAvg, BookSearchRequest req) {
    Order order = req.getDirection() == Direction.ASC ? Order.ASC : Order.DESC;

    return switch (req.getOrderBy()) {
      case title -> new OrderSpecifier<>(order, book.title);
      case publishedDate -> new OrderSpecifier<>(order, book.publishedAt);
      case rating -> new OrderSpecifier<>(order, ratingAvg);
      case reviewCount -> new OrderSpecifier<>(order, reviewCount);
    };
  }

  private OrderSpecifier<?> getSecondaryOrder(QBook book, BookSearchRequest req) {
    Order order = req.getDirection() == Direction.ASC ? Order.ASC : Order.DESC;
    return new OrderSpecifier<>(order, book.createdAt);
  }
  private Comparable<?> parseCursorValue(BookOrderBy orderBy, String cursor) {
    switch (orderBy) {
      case title:
        return cursor;
      case publishedDate:
        return LocalDate.parse(cursor);
      case rating:
        return  Double.valueOf(cursor);
      case reviewCount:
        return Long.valueOf(cursor);
      default:
        throw new IllegalArgumentException("Unknown orderBy: " + orderBy);
    }
  }

  private BooleanExpression buildCursorPredicate(QBook book,
      NumberExpression<Long> reviewCountExpr,
      NumberExpression<Double> ratingAvgExpr,
      BookSearchRequest req) {
    BookOrderBy orderBy = req.getOrderBy();
    Direction direction = req.getDirection();

    Comparable<?> cursorValue = parseCursorValue(orderBy, req.getCursor());
    LocalDateTime after = req.getAfter();

    if (cursorValue == null || after == null) {
      return null;
    }

    BooleanExpression primaryCmp;
    BooleanExpression primaryEq = null;

    switch (orderBy) {
      case title -> {
        String value = (String) cursorValue;
        primaryCmp = direction == Direction.ASC ? book.title.gt(value) : book.title.lt(value);
        primaryEq = book.title.eq(value);
      }
      case publishedDate -> {
        LocalDate value = (LocalDate) cursorValue;
        primaryCmp = direction == Direction.ASC ? book.publishedAt.gt(value) : book.publishedAt.lt(value);
        primaryEq = book.publishedAt.eq(value);
      }
      case rating -> {
        Double value = (Double) cursorValue;
        primaryCmp = direction == Direction.ASC ? ratingAvgExpr.gt(value) : ratingAvgExpr.lt(value);
        primaryEq = ratingAvgExpr.eq(value);
      }
      case reviewCount -> {
        Long value = (Long) cursorValue;
        primaryCmp = direction == Direction.ASC ? reviewCountExpr.gt(value) : reviewCountExpr.lt(value);
        primaryEq = reviewCountExpr.eq(value);
      }
      default -> throw new IllegalArgumentException("Unsupported orderBy: " + orderBy);
    }

    BooleanExpression secondaryCmp = primaryEq.and(
        direction == Direction.ASC ? book.createdAt.gt(after) : book.createdAt.lt(after)
    );
    return primaryCmp.or(secondaryCmp);
  }
}
