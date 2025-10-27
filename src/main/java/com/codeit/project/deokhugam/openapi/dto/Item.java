package com.codeit.project.deokhugam.openapi.dto;

import lombok.Builder;

@Builder
public record Item(
        String title,
        String link,
        String image,
        String author,
        String discount,
        String publisher,
        String pubdate,
        String isbn,
        String description
) {}
