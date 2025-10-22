package com.codeit.project.deokhugam.domain.user.dto;

public record CursorPageResponsePowerUserDto(
    PowerUserDto content,
    String nextCursor,
    String nextAfter,
    Integer size,
    Integer totalElements,
    boolean hasNext
) {

}
