package com.codeit.project.deokhugam.domain.book.mapper;

import com.codeit.project.deokhugam.domain.book.dto.BookDto;
import com.codeit.project.deokhugam.domain.book.entity.Book;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {
  @Mapping(source = "reviewCount", target = "reviewCount")
  @Mapping(source = "rating", target = "rating")
  @Mapping(source = "book.publishedAt", target = "publishedDate")
  BookDto toDto(Book book,long reviewCount, double rating);
}
