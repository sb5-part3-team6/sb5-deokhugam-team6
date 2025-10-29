package com.codeit.project.deokhugam.external.client;

import com.codeit.project.deokhugam.external.client.dto.ClovaOcrRequest;
import com.codeit.project.deokhugam.external.client.dto.ClovaOcrResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class NaverCloudOcrApiClient {

  private final WebClient naverClovaClient;
  private final String secretKey;
  private final ObjectMapper objectMapper;

  private static final Pattern ISBN_PATTERN = Pattern.compile("(978|979)\\d{10}");

  public NaverCloudOcrApiClient(@Qualifier("naverClovaClient") WebClient naverClovaClient,
      @Value("${external.ncp.api.secret-key}") String secretKey,
      ObjectMapper objectMapper) {

    this.naverClovaClient = naverClovaClient;
    this.secretKey = secretKey;
    this.objectMapper = objectMapper;
  }

  public Mono<String> extractIsbnFromImage(MultipartFile imageFile) throws IOException {
    ClovaOcrRequest.Image image = new ClovaOcrRequest.Image(
        imageFile.getContentType()
                 .split("/")[1],
        imageFile.getOriginalFilename()
    );

    ClovaOcrRequest request = new ClovaOcrRequest(
        "V1",
        UUID.randomUUID()
            .toString(),
        Instant.now()
               .toEpochMilli(),
        "ko",
        List.of(image)
    );

    MultipartBodyBuilder builder = new MultipartBodyBuilder();

    try {

      String requestJson = objectMapper.writeValueAsString(request);
      builder.part("message", requestJson, MediaType.APPLICATION_JSON);

    } catch (JsonProcessingException e) {

      log.error("ocr 요청 직렬화 실패", e);
      return Mono.error(e);

    }

    builder.part("file", imageFile.getResource())
           .filename(imageFile.getOriginalFilename())
           .contentType(MediaType.valueOf(imageFile.getContentType()));

    log.info("ocr 호출 시작");

    return naverClovaClient.post()
                           .header("X-OCR-SECRET", secretKey)
                           .contentType(MediaType.MULTIPART_FORM_DATA)
                           .body(BodyInserters.fromMultipartData(builder.build()))
                           .retrieve()
                           .bodyToMono(ClovaOcrResponse.class)
                           .doOnSuccess(response -> log.info("ocr api 응답 수신 성공"))
                           .flatMap(response -> {
                             String isbn = findIsbnInResponse(response);

                             if (isbn != null) {
                               log.info("isbn 추출 성공 : {}", isbn);
                               return Mono.just(isbn);
                             } else {
                               return Mono.empty();
                             }
                           })
                           .doOnError(e -> log.error("api 호출 중 에러 발생 : ", e))
                           .onErrorResume(e -> {
                             log.error("clova api 호출 실패 : ", e);
                             return Mono.empty();
                           });
  }

  public String findIsbnInResponse(ClovaOcrResponse response) {
    if (response == null || response.images() == null || response.images()
                                                                 .isEmpty() ||
        response.images()
                .get(0)
                .fields() == null) {
      log.warn("Clova OCR 결과가 비어있음");
      return null;
    }

    String fullText = response.images()
                              .get(0)
                              .fields()
                              .stream()
                              .map(ClovaOcrResponse.OcrField::inferText)
                              .collect(Collectors.joining(" "));

    if (fullText.isBlank()) {
      log.warn("Clova OCR 결과 텍스트가 비어있음");
      return null;
    }
    log.debug("Clova OCR 결과 원본 텍스트: {}", fullText);

    String cleanedText = fullText.replaceAll("[-\\s]", "");
    log.debug("정제된 텍스트: {}", cleanedText);

    Matcher matcher = ISBN_PATTERN.matcher(cleanedText);
    if (matcher.find()) {
      return matcher.group(0);
    }

    log.warn("정제된 텍스트에서 ISBN 패턴을 찾지 못함");
    return null;
  }
}
