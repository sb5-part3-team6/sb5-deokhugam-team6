package com.codeit.project.deokhugam.domain.book.controller;

import com.codeit.project.deokhugam.domain.book.dto.request.BookCreateRequest;
import com.codeit.project.deokhugam.domain.book.dto.response.BookDto;
import com.codeit.project.deokhugam.domain.book.dto.request.BookPopularRequest;
import com.codeit.project.deokhugam.domain.book.dto.response.BookResponse;
import com.codeit.project.deokhugam.domain.book.dto.request.BookSearchRequest;
import com.codeit.project.deokhugam.domain.book.dto.request.BookUpdateRequest;
import com.codeit.project.deokhugam.domain.book.dto.response.CursorPageResponseBookDto;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

public interface BookApi {

  ResponseEntity<BookDto> create(BookCreateRequest bookData, MultipartFile thumbnailImage);

  ResponseEntity<BookDto> updatePatch(Long bookId, BookUpdateRequest bookData,
      MultipartFile thumbnailImage);

  ResponseEntity<Void> softDelete(@PathVariable Long bookId);

  ResponseEntity<Void> hardDelete(@PathVariable Long bookId);

  ResponseEntity<BookDto> findById(@PathVariable Long bookId);

  ResponseEntity<CursorPageResponseBookDto<BookDto>> search(BookSearchRequest bookSearchReq);

  ResponseEntity<PageResponse> popular(BookPopularRequest bookPopularReq);

  ResponseEntity<BookResponse> getBook(@PathParam("isbn") String isbn);

  Mono<ResponseEntity<String>> extractOcrByClova(MultipartFile imageFile);
}
