package com.codeit.project.deokhugam.openapi.service;

import com.codeit.project.deokhugam.openapi.dto.ClovaOcrResponse;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

public interface OcrService {
  Mono<String> extractIsbnFromImage(MultipartFile imageFile) throws IOException;
  String findIsbnInResponse(ClovaOcrResponse response);
}
