package com.codeit.project.deokhugam.domain.book.mapper;

import com.codeit.project.deokhugam.domain.book.dto.BookDto;
import com.codeit.project.deokhugam.domain.book.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {
  @Mapping(target = "reviewCount", constant = "0")
  @Mapping(target = "rating", constant = "0.0")
  @Mapping(source = "book.publishedAt", target = "publishedDate")
  BookDto toDto(Book book,int reviewCount, double rating);





}
