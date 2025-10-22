package com.codeit.project.deokhugam.domain.book.mapper;

import com.codeit.project.deokhugam.domain.book.dto.BookCreateRequest;
import com.codeit.project.deokhugam.domain.book.dto.BookDto;
import com.codeit.project.deokhugam.domain.book.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class BookMapper {
  @Mapping(source = "publishedAt", target = "publishedDate")
  public abstract BookDto toDto(Book book);

}
