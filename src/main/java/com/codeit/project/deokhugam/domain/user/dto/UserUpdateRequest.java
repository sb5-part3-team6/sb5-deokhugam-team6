package com.codeit.project.deokhugam.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 2, message = "닉네임은 2자 이상이어야 합니다.")
    String nickname
) {

}
