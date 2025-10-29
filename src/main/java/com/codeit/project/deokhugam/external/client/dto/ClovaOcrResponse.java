package com.codeit.project.deokhugam.external.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ClovaOcrResponse(
    List<OcrImage> images
) {
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record OcrImage(
      List<OcrField> fields
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record OcrField(
      String inferText
  ) {}
}
