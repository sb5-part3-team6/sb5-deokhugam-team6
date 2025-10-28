package com.codeit.project.deokhugam.domain.book.storage.impl;

import com.codeit.project.deokhugam.domain.book.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("prod")
public class FileStorageS3 implements FileStorage {
  private final S3Client s3Client;

  @Value("${aws.s3.bucket}")
  private String bucketName;

  @Value("${aws.s3.prefix:uploads}")
  private String prefix;

  @Value("${aws.region}")
  private String region;

  @Value("${app.cdn.base-url}")
  private String cdnBaseUrl;

  //private String ext =".jpg";

  @Override
  public void saveThumbnailImage(String isbn, MultipartFile thumbnailImage) {
    String key = String.format("%s/thumbnails/%s",prefix,isbn);
    deleteFromS3(key);
    uploadToS3(key,thumbnailImage);
    log.info("썸네일 업로드 완료 : {}", key);
  }

  @Override
  public String getThumbnailImage(String isbn) {
    String key = String.format("%s/thumbnails/%s",prefix,isbn);
    return String.format("%s/%s",cdnBaseUrl,key);
  }

  @Override
  public void deleteThumbnailImage(String isbn) {
    String key = String.format("%s/thumbnails/%s",prefix,isbn);
    deleteFromS3(key);
    log.info("썸네일 삭제 완료 : {}",key);
  }

  private void uploadToS3(String key,MultipartFile file) {
    try {
      PutObjectRequest putReq = PutObjectRequest.builder()
          .bucket(bucketName)
          .key(key)
          .contentType(file.getContentType())
          .build();

      s3Client.putObject(putReq, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    } catch (Exception e) {
      throw new RuntimeException("S3 업로드 실패: " + key, e);
    }
  }

  private void deleteFromS3(String key) {
    try {
      s3Client.deleteObject(DeleteObjectRequest.builder()
          .bucket(bucketName)
          .key(key)
          .build());
    } catch (Exception e) {
      log.warn("S3 삭제 실패 (무시 가능): {}", key,e);
    }
  }
}
