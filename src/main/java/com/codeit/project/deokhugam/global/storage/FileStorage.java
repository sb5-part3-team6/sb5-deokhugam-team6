package com.codeit.project.deokhugam.global.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {
  void saveThumbnailImage(String isbn, MultipartFile thumbnailImage);
  String getThumbnailImage(String isbn);
  void  deleteThumbnailImage(String isbn);
}