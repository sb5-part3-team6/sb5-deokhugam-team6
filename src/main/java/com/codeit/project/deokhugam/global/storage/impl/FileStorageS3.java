package com.codeit.project.deokhugam.global.storage.impl;

import com.codeit.project.deokhugam.domain.book.dto.response.BookResponse;
import com.codeit.project.deokhugam.domain.book.entity.Book;
import com.codeit.project.deokhugam.global.storage.FileStorage;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

  private static final String THUMBNAIL_PATH = "thumbnails";
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");


  @Override
  public String saveThumbnailImage(Book book, MultipartFile thumbnailImage) {
    String timestamp = LocalDateTime.now().format(FORMATTER);
    String key = String.format("%s/%s/%s_%s",prefix,THUMBNAIL_PATH,book.getIsbn(),timestamp);
    if (book.getThumbnailUrl() != null && !book.getThumbnailUrl().isEmpty()) {
      deleteFromS3(extractKeyFromUrl(book.getThumbnailUrl()));
    }
    uploadToS3(key,thumbnailImage);
    log.info("썸네일 업로드 완료 : {}", key);
    return getFullUrl(key);
  }
  //api로 받은 정보
  public String saveThumbnailImage(BookResponse bookResponse, InputStream inputStream, String contentType, long contentLength ) {
    String timestamp = LocalDateTime.now().format(FORMATTER);
    String key = String.format("%s/%s/%s", prefix, THUMBNAIL_PATH,bookResponse.isbn(),timestamp);
    if (bookResponse.thumbnailImage() != null && !bookResponse.thumbnailImage().isEmpty()) {
      deleteFromS3(extractKeyFromUrl(bookResponse.thumbnailImage()));
    }
    uploadStreamToS3(key, inputStream, contentType, contentLength);
    log.info("썸네일 업로드 완료 (스트림) : {}", key);
    return getFullUrl(key);
  }

  private String getFullUrl(String key) {
    return String.format("%s/%s", cdnBaseUrl, key);
  }
  private String extractKeyFromUrl(String fullUrl) {
    if(fullUrl.contains(cdnBaseUrl)) {
      return fullUrl.substring(cdnBaseUrl.length()).replaceAll("^/|/$", "");
    }
    return fullUrl;
  }

  @Override
  public String getThumbnailImage(Book book) {
    return book.getThumbnailUrl();
  }

  @Override
  public void deleteThumbnailImage(String url) {
    deleteFromS3(extractKeyFromUrl(url));
    log.info("썸네일 삭제 완료 : {}",url);
  }

  private void uploadToS3(String key,MultipartFile file) {
    try {
      PutObjectRequest putReq = PutObjectRequest.builder()
          .bucket(bucketName)
          .key(key)
          .contentType(file.getContentType())
          .contentLength(file.getSize())
          .build();

      s3Client.putObject(putReq, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    } catch (Exception e) {
      throw new RuntimeException("S3 업로드 실패: " + key, e);
    }
  }

  private void uploadStreamToS3(String key,InputStream inputStream, String contentType, long contentLength) {
    try {
      PutObjectRequest putReq = PutObjectRequest.builder()
          .bucket(bucketName)
          .key(key)
          .contentType(contentType)
          .contentLength(contentLength)
          .build();
      s3Client.putObject(putReq, RequestBody.fromInputStream(inputStream, contentLength));
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
