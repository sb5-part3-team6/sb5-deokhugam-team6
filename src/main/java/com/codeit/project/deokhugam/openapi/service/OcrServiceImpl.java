package com.codeit.project.deokhugam.openapi.service;

import com.codeit.project.deokhugam.openapi.dto.OcrSpaceResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OcrServiceImpl implements OcrService {

  private final WebClient ocrSpaceWebClient;
  private final String apiKey;

  private static final Pattern ISBN_PATTERN = Pattern.compile("(978|979)\\d{10}");

  public OcrServiceImpl(WebClient ocrSpaceWebClient, @Value("${ocr-space.api-key}") String apiKey) {
    this.ocrSpaceWebClient = ocrSpaceWebClient;
    this.apiKey = apiKey;
  }

  // 이미지 파일 -> ocr space api에 전송 후 isbn 추출
  //
  @Override
  public Mono<String> extractIsbnFromImage(MultipartFile imageFile) throws IOException {
    MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
    map.add("apikey", apiKey);
    map.add("language", "eng");
    map.add("isOverlayRequired", "false");

    // multiPartFile -> byteArrayResource후 map.add
    ByteArrayResource imageResource = new ByteArrayResource(imageFile.getBytes()) {
      @Override
      public String getFilename() {
        return imageFile.getOriginalFilename();
      }
    };
    map.add("file", imageResource);

    return ocrSpaceWebClient.post()
        .uri("/parse/image")
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .body(BodyInserters.fromMultipartData(map))
        .retrieve()
        .bodyToMono(OcrSpaceResponse.class)
        .map(this::findIsbnInResponse)
        .onErrorResume(error -> {
          log.error("OCR API 호출 실 : ", error.getMessage());
          return Mono.empty();
        });
  }

  @Override
  public String findIsbnInResponse(OcrSpaceResponse response) {
    if(response == null || response.isErroredOnProcessing() || response.parsedResults() == null || response.parsedResults().isEmpty()) {
      log.error("OCR 파싱 오류 : ", (response != null ? response.errorMessage() : "응답 없음"));
      return null;
    }

    String fullText = response.parsedResults().get(0).parsedText();
    if(fullText == null || fullText.isEmpty()) {
      return null;
    }

    String text = fullText.replaceAll("[-\\s]", "");

    Matcher matcher = ISBN_PATTERN.matcher(text);
    if(matcher.find()) {
      return matcher.group(0);
    }

    return null;
    // TODO : Controller에서 API 호출하기
  }
}
