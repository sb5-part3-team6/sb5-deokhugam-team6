package com.codeit.project.deokhugam.domain.comment.service;

import com.codeit.project.deokhugam.domain.comment.dto.CommentCreateRequest;
import com.codeit.project.deokhugam.domain.comment.dto.CommentDto;
import com.codeit.project.deokhugam.domain.comment.dto.CommentUpdateRequest;
import com.codeit.project.deokhugam.domain.comment.dto.CursorPageResponseCommentDto;

import java.time.LocalDateTime;

public interface CommentService {
    CommentDto create(CommentCreateRequest req);
    CommentDto update(Long id,CommentUpdateRequest req);
    void deleteSoft(Long id);
    void delete(Long id);
    CommentDto findById(Long id);
    CursorPageResponseCommentDto getCommentsByCursor(Long reviewId, LocalDateTime after, Long cursor, int limit, String direction);

}
