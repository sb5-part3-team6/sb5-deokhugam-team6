package com.codeit.project.deokhugam.global.config.impl;

import com.codeit.project.deokhugam.global.config.FileConfig;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class FileConfigDev implements FileConfig {

  private String uploadDir;

  @PostConstruct
  public void init(){
    String os = System.getProperty("os.name").toLowerCase();
    if(os.contains("win")){
      uploadDir = "C:/uploads/";
    }else{
      uploadDir = "/var/uploads/";
    }

    File dir = new File(uploadDir);
    if(!dir.exists()){
      boolean create = dir.mkdir();
      if(create){
        System.out.println("업로드 디렉토리 생성 완료 : "+ uploadDir);
      }else{
        System.out.println("업로드 디렉토리 생성 실패 : "+ uploadDir);
      }
    }
  }

  @Override
  public File getThumbnailUploadDirFile() {
    Path dir = Paths.get(uploadDir,"thumbnails");
    if(Files.notExists(dir)){
      try {
        Files.createDirectories(dir);
      } catch (Exception e) {
        throw new RuntimeException("썸네일 업로드 디렉토리 생성 실패");
      }
    }
    return dir.toFile();
  }
}
