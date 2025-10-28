package com.codeit.project.deokhugam.domain.book.controller;

import com.codeit.project.deokhugam.domain.book.dto.*;
import com.codeit.project.deokhugam.domain.book.service.BookService;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import com.codeit.project.deokhugam.openapi.api.NaverBookApiClient;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final NaverBookApiClient naverBookApiClient;
    private final BookService bookService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BookDto> create(
        @RequestPart(name = "bookData")@Valid BookCreateRequest bookData,
        @RequestPart(value="thumbnailImage",required = false) MultipartFile thumbnailImage) {
        BookDto bookDto = bookService.create(bookData,thumbnailImage);

        return ResponseEntity.status(HttpStatus.CREATED).body(bookDto);

    }

    @PatchMapping(path ="{bookId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BookDto> updatePatch(
        @PathVariable Long bookId,
        @RequestPart("bookData")@Valid BookUpdateRequest bookData,
        @RequestPart(value="thumbnailImage",required = false)MultipartFile thumbnailImage
    ){
        BookDto updatedBookDto = bookService.update(bookId,bookData,thumbnailImage);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBookDto);
    }

    @DeleteMapping(path="{bookId}")
    public ResponseEntity<Void> softDelete(@PathVariable Long bookId) {
        bookService.softDelete(bookId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(path = "{bookId}/hard")
    public ResponseEntity<Void> hardDelete(@PathVariable Long bookId) {
        bookService.hardDelete(bookId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(path = "{bookId}")
    public ResponseEntity<BookDto> findById(@PathVariable Long bookId) {
        BookDto findBookDto = bookService.findById(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(findBookDto);
    }

    @GetMapping
    public ResponseEntity<CursorPageResponseBookDto<BookDto>>search(
        BookSearchRequest bookSearchReq
    ){
        CursorPageResponseBookDto<BookDto> searchBook= bookService.search(bookSearchReq);
        return ResponseEntity.status(HttpStatus.OK).body(searchBook);
    }

    @GetMapping("/popular")
    public ResponseEntity<PageResponse> popular(BookPopularRequest bookPopularReq){
        PageResponse response = bookService.popularList(bookPopularReq);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<BookResponse> getBook(@PathParam("isbn") String isbn) {
        BookResponse response = naverBookApiClient.fetchBooks(isbn);
        return ResponseEntity.ok(response);
    }
}
