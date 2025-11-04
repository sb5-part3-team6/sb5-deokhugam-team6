package com.codeit.project.deokhugam.global.storage;

import com.codeit.project.deokhugam.domain.book.dto.response.BookResponse;
import com.codeit.project.deokhugam.domain.book.entity.Book;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {
  String saveThumbnailImage(Book book, MultipartFile thumbnailImage);
  String saveThumbnailImage(BookResponse book, InputStream inputStream, String contentType, long contentLength);
  String getThumbnailImage(Book book);
  void  deleteThumbnailImage(String url);
}