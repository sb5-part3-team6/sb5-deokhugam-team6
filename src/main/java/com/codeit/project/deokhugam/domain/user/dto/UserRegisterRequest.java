package com.codeit.project.deokhugam.domain.user.dto;

public record UserRegisterRequest(
    String email,
    String nickname,
    String password
) {

}
