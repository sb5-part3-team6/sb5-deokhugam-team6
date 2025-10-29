package com.codeit.project.deokhugam.global.config.impl;

import com.codeit.project.deokhugam.global.config.FileConfig;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class FileConfigProd implements FileConfig {

  @Value("${file.upload-dir}")
  private String uploadDir;

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(Path.of(uploadDir));
    } catch (Exception e) {
      throw new IllegalStateException("업로드 디렉토리 생성 실패 : " + uploadDir, e);
    }
  }

  @Override
  public File getThumbnailUploadDirFile() {
    try {
      Path p = Paths.get(uploadDir, "thumbnails");
      Files.createDirectories(p);
      return p.toFile();
    } catch (Exception e) {
      throw new IllegalStateException("아바타 디렉토리 생성 실패", e);
    }
  }
}



