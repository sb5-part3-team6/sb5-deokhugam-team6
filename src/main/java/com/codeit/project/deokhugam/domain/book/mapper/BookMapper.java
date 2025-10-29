package com.codeit.project.deokhugam.domain.book.mapper;

import com.codeit.project.deokhugam.domain.book.dto.response.BookDto;
import com.codeit.project.deokhugam.domain.book.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {
  @Mapping(source = "reviewCount", target = "reviewCount")
  @Mapping(source = "rating", target = "rating")
  @Mapping(source = "book.publishedAt", target = "publishedDate")
  BookDto toDto(Book book,long reviewCount, double rating);
}
