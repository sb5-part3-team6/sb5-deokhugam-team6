package com.codeit.project.deokhugam.domain.book.dto.response;

import java.util.List;

public record CursorPageResponseBookDto <T>(
    List<T> content,
    String nextCursor,
    String nextAfter,
    Integer size,
    Integer totalElements,
    String hasNext
){

}
