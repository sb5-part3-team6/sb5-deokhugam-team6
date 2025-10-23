package com.codeit.project.deokhugam.domain.comment.controller;

import com.codeit.project.deokhugam.domain.comment.dto.CommentCreateRequest;
import com.codeit.project.deokhugam.domain.comment.dto.CommentDto;
import com.codeit.project.deokhugam.domain.comment.dto.CommentUpdateRequest;
import com.codeit.project.deokhugam.domain.comment.service.CommentService;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

  private final CommentService commentService;

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

  @GetMapping
  public ResponseEntity<PageResponse> getByCursor(
      @RequestParam Long reviewId,
      @RequestParam(required = false) LocalDateTime after,
      @RequestParam(required = false) Long cursor,
      @RequestParam(defaultValue = "20") int limit,
      @RequestParam(defaultValue = "DESC") String direction
  ) {
    PageResponse response =
        commentService.getByCursor(reviewId, after, cursor, limit, direction);

    return ResponseEntity.ok(response);
  }


  @PutMapping("/{commentId}")
  public ResponseEntity<CommentDto> update(@PathVariable Long commentId,
      @RequestBody CommentUpdateRequest req) {
    CommentUpdateRequest updated = new CommentUpdateRequest(req.content());
    CommentDto updatedComment = commentService.update(commentId, updated);
    return ResponseEntity.ok(updatedComment);
  }

  @DeleteMapping("/{commentId}")
  public ResponseEntity<Void> deleteSoft(@PathVariable Long commentId) {
    commentService.deleteSoft(commentId);
    return ResponseEntity.noContent()
                         .build();
  }

  @DeleteMapping("/{commentId}/hard")
  public ResponseEntity<Void> deleteHard(@PathVariable Long commentId) {
    commentService.delete(commentId);
    return ResponseEntity.noContent()
                         .build();
  }
}
