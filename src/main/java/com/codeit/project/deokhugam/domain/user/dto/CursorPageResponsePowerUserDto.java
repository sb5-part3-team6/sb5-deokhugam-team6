package com.codeit.project.deokhugam.domain.user.dto;

import java.util.List;

public record CursorPageResponsePowerUserDto(
    List<PowerUserDto> content,
    String nextCursor,
    String nextAfter,
    Integer size,
    Integer totalElements,
    boolean hasNext
) {

}
