package com.codeit.project.deokhugam.domain.book.mapper;

import com.codeit.project.deokhugam.domain.book.dto.BookCreateRequest;
import com.codeit.project.deokhugam.domain.book.dto.BookDto;
import com.codeit.project.deokhugam.domain.book.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class BookMapper {
  public abstract BookDto toDto(Book book);

  @Mapping(source = "publishedDate", target = "publishedAt")
  public abstract Book toBook(BookCreateRequest bookCreateRequest);

}
