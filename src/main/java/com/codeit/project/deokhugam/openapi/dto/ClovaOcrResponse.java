package com.codeit.project.deokhugam.openapi.dto;

import java.util.List;

public record ClovaOcrResponse(
    List<OcrImage> images
) {
  public record OcrImage(
      List<OcrField> fields
  ) {
    public record OcrField(
        String inferText
    ) {}
  }
}
