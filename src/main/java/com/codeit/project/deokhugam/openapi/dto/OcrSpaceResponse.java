package com.codeit.project.deokhugam.openapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OcrSpaceResponse(
    @JsonProperty("ParsedResults")
    List<ParsedResult> parsedResults,

    @JsonProperty("IsErroredOnProcessing")
    boolean isErroredOnProcessing,

    @JsonProperty("ErrorMessage")
    String errorMessage
) {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record ParsedResult(
      @JsonProperty("ParsedText")
      String parsedText
  ) {}

}
