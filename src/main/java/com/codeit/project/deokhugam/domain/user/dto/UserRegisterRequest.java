package com.codeit.project.deokhugam.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(
    @NotBlank(message = "이메일은 필수입니다.")
    @Pattern(
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
        message = "유효하지 않은 이메일 형식입니다.")
    String email,

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 2, message = "닉네임은 2자 이상이어야 합니다.")
    String nickname,

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d!@#$%^&*()_+\\-=\\[\\]{};':\"|,.<>\\/?]{8,}$",
        message = "비밀번호는 8자 이상이, 영문과 숫자를 포함해야 합니다.")
    String password

) {

}
