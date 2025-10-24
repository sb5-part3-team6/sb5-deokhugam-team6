package com.codeit.project.deokhugam.domain.comment.controller;

import com.codeit.project.deokhugam.domain.comment.dto.CommentCreateRequest;
import com.codeit.project.deokhugam.domain.comment.dto.CommentDto;
import com.codeit.project.deokhugam.domain.comment.dto.CommentUpdateRequest;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Tag(name = "댓글 관리", description = "댓글 관련 API")
public interface CommentAPI {

    @Operation(summary = "리뷰 댓글 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공", content = @Content(schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "리뷰 정보 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")})
    ResponseEntity<PageResponse> getByCursor(
            Long reviewId,
            String direction,
            Long cursor,
            LocalDateTime after,
            Integer limit
    );

    @Operation(summary = "댓글 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "댓글 등록 성공", content = @Content(schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "리뷰 정보 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")})
    ResponseEntity<CommentDto> create(CommentCreateRequest request);

    @Operation(summary = "댓글 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 조회 성공", content = @Content(schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "404", description = "댓글 정보 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")})
    ResponseEntity<CommentDto> findById(Long commentId);

    @Operation(summary = "댓글 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = @Content(schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "댓글 수정 권한 없음"),
            @ApiResponse(responseCode = "404", description = "댓글 정보 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")})
    ResponseEntity<CommentDto> update(Long commentId, CommentUpdateRequest request, String userId);

    @Operation(summary = "댓글 논리 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "댓글 삭제 권한 없음"),
            @ApiResponse(responseCode = "404", description = "댓글 정보 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")})
    ResponseEntity<Void> deleteSoft(Long commentId, String userId);

    @Operation(summary = "댓글 물리 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "댓글 삭제 권한 없음"),
            @ApiResponse(responseCode = "404", description = "댓글 정보 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")})
    ResponseEntity<Void> deleteHard(Long commentId, String userId);
}
