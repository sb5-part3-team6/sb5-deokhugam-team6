package com.codeit.project.deokhugam.global.config.impl;

import com.codeit.project.deokhugam.global.config.FileConfig;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
@Profile("dev")
public class FileConfigDev implements FileConfig {

  @Value("${file.upload-dir}")
  private String uploadDir;

  private Path uploadPath;

  @PostConstruct
  public void init() {
    try {
      uploadPath = Paths.get(uploadDir)
                        .toAbsolutePath()
                        .normalize();
      Files.createDirectories(uploadPath);
    } catch (Exception e) {
      throw new IllegalStateException("업로드 디렉토리 생성 실패 : " + uploadDir, e);
    }
  }

  @Override
  public File getThumbnailUploadDirFile() {
    try {
      Path p = uploadPath.resolve("thumbnails");
      Files.createDirectories(p);
      return p.toFile();
    } catch (Exception e) {
      throw new IllegalStateException("썸네일 디렉토리 생성 실패", e);
    }
  }
}