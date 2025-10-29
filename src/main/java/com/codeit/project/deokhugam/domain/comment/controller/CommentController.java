package com.codeit.project.deokhugam.domain.comment.controller;

import com.codeit.project.deokhugam.domain.comment.dto.request.CommentCreateRequest;
import com.codeit.project.deokhugam.domain.comment.dto.response.CommentDto;
import com.codeit.project.deokhugam.domain.comment.dto.request.CommentUpdateRequest;
import com.codeit.project.deokhugam.domain.comment.service.CommentService;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController implements CommentAPI {

  private final CommentService commentService;

  @GetMapping
  public ResponseEntity<PageResponse> getByCursor(
          @RequestParam Long reviewId,
          @RequestParam(defaultValue = "DESC") String direction,
          @RequestParam(required = false) Long cursor,
          @RequestParam(required = false) LocalDateTime after,
          @RequestParam(defaultValue = "20") Integer limit
  ) {
    PageResponse response =
            commentService.getByCursor(reviewId, after, cursor, limit, direction);

    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<CommentDto> create(@RequestBody CommentCreateRequest req) {
    CommentDto saved = commentService.create(req);
    return ResponseEntity.status(HttpStatus.CREATED)
                         .body(saved);
  }

  @GetMapping("/{commentId}")
  public ResponseEntity<CommentDto> findById(@PathVariable Long commentId) {
    CommentDto comment = commentService.findById(commentId);
    return ResponseEntity.ok(comment);
  }

  @PatchMapping("/{commentId}")
  public ResponseEntity<CommentDto> update(@PathVariable Long commentId,
      @RequestBody CommentUpdateRequest req,
      @RequestHeader("Deokhugam-Request-User-ID") String userId) {
    Long currentUserId = Long.parseLong(userId);
    CommentDto updatedComment = commentService.update(commentId, req, currentUserId);
    return ResponseEntity.ok(updatedComment);
  }

  @DeleteMapping("/{commentId}")
  public ResponseEntity<Void> deleteSoft(@PathVariable Long commentId,
    @RequestHeader("Deokhugam-Request-User-ID") String userId) {
    Long currentUserId = Long.parseLong(userId);
    commentService.deleteSoft(commentId, currentUserId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{commentId}/hard")
  public ResponseEntity<Void> deleteHard(@PathVariable Long commentId,
    @RequestHeader("Deokhugam-Request-User-ID") String userId) {
    Long currentUserId = Long.parseLong(userId);
    commentService.delete(commentId, currentUserId);
    return ResponseEntity.noContent()
                         .build();
  }
}
