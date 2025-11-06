package com.codeit.project.deokhugam.global.storage.impl;

import com.codeit.project.deokhugam.domain.book.dto.response.BookResponse;
import com.codeit.project.deokhugam.domain.book.entity.Book;
import com.codeit.project.deokhugam.global.config.FileConfig;
import com.codeit.project.deokhugam.global.storage.FileStorage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class FileStorageDev implements FileStorage {

  private final FileConfig fileConfig;

  @Override
  public String saveThumbnailImage(Book book, MultipartFile thumbnailImage) {
    File dir = fileConfig.getThumbnailUploadDirFile();
    File dest = new File(dir, book.getIsbn());
    if (dest.exists()) {
      boolean deleted = dest.delete();
      if (!deleted) {
        throw new RuntimeException("기존 썸네일 삭제 실패 : " + dest.getAbsolutePath());
      }
    }
    try {
      thumbnailImage.transferTo(dest);
    } catch (IOException e) {
      throw new RuntimeException("썸네일 파일 저장 실패 : " + dest.getAbsolutePath(), e);
    }
    return ServletUriComponentsBuilder.fromCurrentContextPath()
                                      .path("/thumbnails/")
                                      .path(book.getIsbn())
                                      .toUriString();
  }

  @Override
  public String saveThumbnailImage(BookResponse book, InputStream inputStream, String contentType,
      long contentLength) {
    return "";
  }

  @Override
  public String getThumbnailImage(Book book) {
    return ServletUriComponentsBuilder.fromCurrentContextPath()
                                      .path("/thumbnails/")
                                      .path(book.getIsbn())
                                      .toUriString();
  }

  @Override
  public void deleteThumbnailImage(String isbn) {
    File dir = fileConfig.getThumbnailUploadDirFile();
    File dest = new File(dir, isbn);
    if (dest.exists()) {
      boolean deleted = dest.delete();
      if (!deleted) {
        throw new RuntimeException("기존 썸네일 삭제 실패 : " + dest.getAbsolutePath());
      }
    }
    System.out.println("썸네일 삭제 완료 : " + dest.getAbsolutePath());
  }
}