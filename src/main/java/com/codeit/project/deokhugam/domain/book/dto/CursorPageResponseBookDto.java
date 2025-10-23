package com.codeit.project.deokhugam.domain.book.dto;

import java.util.List;

public record CursorPageResponseBookDto <T>(
    List<T> content,
    String nextCursor,
    String nextAfter,
    String sizeExpand,
    String totalElements,
    String hasNext
){

}
