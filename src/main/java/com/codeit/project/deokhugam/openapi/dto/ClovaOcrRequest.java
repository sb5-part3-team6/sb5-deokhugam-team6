package com.codeit.project.deokhugam.openapi.dto;

import java.util.List;

public record ClovaOcrRequest(
    String version,
    String requestId,
    long timestamp, // Integer
    String lang,
    List<Image> images

) {
  public record Image(
      String format,
      String name
  ) {}

}
