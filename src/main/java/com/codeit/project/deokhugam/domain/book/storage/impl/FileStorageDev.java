package com.codeit.project.deokhugam.domain.book.storage.impl;

import com.codeit.project.deokhugam.domain.book.config.FileConfig;
import com.codeit.project.deokhugam.domain.book.storage.FileStorage;
import java.io.File;
import java.io.IOException;
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
  public void saveThumbnailImage(String isbn, MultipartFile thumbnailImage) {
    File dir = fileConfig.getThumbnailUploadDirFile();
    File dest = new File(dir, isbn);
    if(dest.exists()){
      boolean deleted = dest.delete();
      if(!deleted){
        throw new RuntimeException("기존 썸네일 삭제 실패 : " + dest.getAbsolutePath());
      }
    }
    try{
      thumbnailImage.transferTo(dest);
    }catch(IOException e){
      throw new RuntimeException("썸네일 파일 저장 실패 : "+ dest.getAbsolutePath(), e);
    }
    System.out.println("아바타 저장 완료 : " + dest.getAbsolutePath());
  }

  @Override
  public String getThumbnailImage(String isbn) {
    return ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/thumbnails/")
        .path(isbn)
        .toUriString();
  }
}
