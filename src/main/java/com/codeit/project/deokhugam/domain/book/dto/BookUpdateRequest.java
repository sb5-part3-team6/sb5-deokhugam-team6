package com.codeit.project.deokhugam.domain.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record BookUpdateRequest(
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max=150, message="글자수를 150자 이하로 입력해주세요")
    String title,

    @NotBlank(message = "저자을 입력해주세요.")
    @Size(max=50, message="글자수를 50자 이하로 입력해주세요")
    String author,

    @NotBlank(message = "설명을 입력해주세요.")
    @Size(max=1000, message="글자수를 1000자 이하로 입력해주세요")
    String description,

    @NotBlank(message = "출판사를 입력해주세요.")
    @Size(max=50, message="글자수를 50자 이하로 입력해주세요")
    String publisher,

    @NotNull(message = "출판일을 선택해주세요.")
    LocalDate publishedDate
) {

}
