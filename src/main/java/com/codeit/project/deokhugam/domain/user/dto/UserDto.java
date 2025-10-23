package com.codeit.project.deokhugam.domain.user.dto;

import java.time.LocalDateTime;

public record UserDto(
    String id,
    String email,
    String nickname,
    LocalDateTime createdAt
) {

}
