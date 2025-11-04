package com.codeit.project.deokhugam.global.storage;

import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {
  void saveThumbnailImage(String isbn, MultipartFile thumbnailImage);
  void saveThumbnailImage(String isbn, InputStream inputStream, String contentType, long contentLength);
  String getThumbnailImage(String isbn);
  void  deleteThumbnailImage(String isbn);
}