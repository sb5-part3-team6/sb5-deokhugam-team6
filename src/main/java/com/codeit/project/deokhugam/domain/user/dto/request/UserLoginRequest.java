package com.codeit.project.deokhugam.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserLoginRequest(
    @NotBlank
    @Pattern(
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
        message = "유효하지 않은 이메일 형식입니다."
    )
    String email,

    @NotBlank(message = "비밀번호는 필수입니다.")
    String password
) {

}
