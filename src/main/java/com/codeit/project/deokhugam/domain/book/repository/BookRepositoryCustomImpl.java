package com.codeit.project.deokhugam.domain.book.repository;

import com.codeit.project.deokhugam.domain.book.dto.BookDto;
import com.codeit.project.deokhugam.domain.book.dto.BookOrderBy;
import com.codeit.project.deokhugam.domain.book.dto.BookSearchRequest;
import com.codeit.project.deokhugam.domain.book.dto.Direction;
import com.codeit.project.deokhugam.domain.book.entity.QBook;
import com.codeit.project.deokhugam.domain.review.entity.QReview;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<BookDto> findBooks(BookSearchRequest bookSearchReq, int pageSize) {
    QBook book = QBook.book;
    QReview review = QReview.review;

    NumberExpression<Long> reviewCount = review.id.countDistinct().coalesce(0L);
    NumberExpression<Double> avgExpression = review.rating.avg().coalesce(0.0);
    NumberExpression<Double> ratingAvg = Expressions.numberTemplate(
        Double.class,
        "ROUND({0}, 1)",
        avgExpression
    );

    //검색 조건
    BooleanBuilder where = new BooleanBuilder();
    if(bookSearchReq.keyword()!=null && !bookSearchReq.keyword().isBlank()){
      String keyword = "%"+bookSearchReq.keyword()+"%";
      where.and(
        book.title.containsIgnoreCase(keyword)
            .or(book.author.likeIgnoreCase(keyword))
            .or(book.isbn.likeIgnoreCase(keyword))
          ).and(book.deletedAt.isNull());
    }

    //정렬조건
    OrderSpecifier<?> primaryOrder = getPrimaryOrder(book,reviewCount, ratingAvg,bookSearchReq);
    OrderSpecifier<?> secondaryOrder = getSecondaryOrder(book, bookSearchReq);

    //커서
    if(bookSearchReq.cursor() !=null && bookSearchReq.after()!=null){
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
    Order order = switch (req.direction()){
      case "ASC" -> Order.ASC;
      case "DESC" -> Order.DESC;
      default -> Order.DESC;
    };
//    Order order = req.direction() == "ASC" ? Order.ASC : Order.DESC;

    return switch (req.orderBy()) {
      case "title" -> new OrderSpecifier<>(order, book.title);
      case "publishedDate" -> new OrderSpecifier<>(order, book.publishedAt);
      case "rating" -> new OrderSpecifier<>(order, ratingAvg);
      case "reviewCount" -> new OrderSpecifier<>(order, reviewCount);
      default -> new OrderSpecifier<>(order, book.title);
    };
  }

  private OrderSpecifier<?> getSecondaryOrder(QBook book, BookSearchRequest req) {
    Order order = switch (req.direction()){
      case "ASC" -> Order.ASC;
      case "DESC" -> Order.DESC;
      default -> Order.DESC;
    };
    return new OrderSpecifier<>(order, book.createdAt);
  }
  private Comparable<?> parseCursorValue(String orderBy, String cursor) {
    switch (orderBy) {
      case "title":
        return cursor;
      case "publishedDate":
        return LocalDate.parse(cursor);
      case "rating":
        return  Double.valueOf(cursor);
      case "reviewCount":
        return Long.valueOf(cursor);
      default:
        throw new IllegalArgumentException("Unknown orderBy: " + orderBy);
    }
  }

  private BooleanExpression buildCursorPredicate(QBook book,
      NumberExpression<Long> reviewCountExpr,
      NumberExpression<Double> ratingAvgExpr,
      BookSearchRequest req) {
    String orderBy = req.orderBy();
    String direction = req.direction();

    Comparable<?> cursorValue = parseCursorValue(orderBy, req.cursor());
    LocalDateTime after = req.after();

    if (cursorValue == null || after == null) {
      return null;
    }

    BooleanExpression primaryCmp;
    BooleanExpression primaryEq = null;

    switch (orderBy) {
      case "title" -> {
        String value = (String) cursorValue;
        primaryCmp = direction == "ASC" ? book.title.gt(value) : book.title.lt(value);
        primaryEq = book.title.eq(value);
      }
      case "publishedDate" -> {
        LocalDate value = (LocalDate) cursorValue;
        primaryCmp = direction == "ASC" ? book.publishedAt.gt(value) : book.publishedAt.lt(value);
        primaryEq = book.publishedAt.eq(value);
      }
      case "rating" -> {
        Double value = (Double) cursorValue;
        primaryCmp = direction == "ASC" ? ratingAvgExpr.gt(value) : ratingAvgExpr.lt(value);
        primaryEq = ratingAvgExpr.eq(value);
      }
      case "reviewCount" -> {
        Long value = (Long) cursorValue;
        primaryCmp = direction == "ASC" ? reviewCountExpr.gt(value) : reviewCountExpr.lt(value);
        primaryEq = reviewCountExpr.eq(value);
      }
      default -> throw new IllegalArgumentException("Unsupported orderBy: " + orderBy);
    }

    BooleanExpression secondaryCmp = primaryEq.and(
        direction == "ASC" ? book.createdAt.gt(after) : book.createdAt.lt(after)
    );
    return primaryCmp.or(secondaryCmp);
  }
}
