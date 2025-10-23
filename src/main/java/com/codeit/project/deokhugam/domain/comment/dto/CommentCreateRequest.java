package com.codeit.project.deokhugam.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentCreateRequest(
    @NotBlank
    Long reviewId,

    @NotBlank
    Long userId,

    @NotBlank
    String content
)
{ }
