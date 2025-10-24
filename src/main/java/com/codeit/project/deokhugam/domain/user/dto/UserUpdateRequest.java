package com.codeit.project.deokhugam.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record UserUpdateRequest(
    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 2, message = "닉네임은 2자 이상이어야 합니다.")
    String nickname
) {

    @JsonCreator
    public static UserUpdateRequest req(String request) {
        if(request == null || request.isBlank()) {
            log.error("닉네임이 입력되지 않음.");
            throw new IllegalArgumentException("닉네임은 필수입니다.");
        }

        if(request.length() < 2) {
            log.error("닉네임이 너무 짧음. Nickname = {}", request);
            throw new IllegalArgumentException("닉네임은 2자 이상이어야 합니다.");
        }

        return new UserUpdateRequest(request);
    }
}
