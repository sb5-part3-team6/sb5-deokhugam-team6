package com.codeit.project.deokhugam.domain.user.controller;

import com.codeit.project.deokhugam.domain.user.dto.*;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "사용자 관리", description = "사용자 관련 API")
public interface UserApi {

  @Operation(summary = "회원가입")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "201", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = UserDto.class))),
          @ApiResponse(responseCode = "400", description = "잘못된 요청 (입력값 검증 실패 : email, id, password)"),
          @ApiResponse(responseCode = "409", description = "이메일 중복"),
          @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      }
  )
  ResponseEntity<UserDto> register(@RequestBody UserRegisterRequest request);

  @Operation(summary = "로그인")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = UserDto.class))),
          @ApiResponse(responseCode = "400", description = "잘못된 요청 (입력값 검증 실패)"),
          @ApiResponse(responseCode = "401", description = "로그인 실패 (이메일 또는 비밀번호 불일치)"),
          @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      }
  )
  ResponseEntity<UserDto> login(@RequestBody UserLoginRequest request);

  @Operation(summary = "사용자 정보 조회")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공", content = @Content(schema = @Schema(implementation = UserDto.class))),
          @ApiResponse(responseCode = "404", description = "사용자 정보 없음"),
          @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      }
  )
  ResponseEntity<UserDto> find(@Parameter(description = "User ID") @PathVariable String userId);

  @Operation(summary = "사용자 논리 삭제")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "204", description = "사용자 삭제 성공"),
          @ApiResponse(responseCode = "403", description = "사용자 삭제 권한 없음"),
          @ApiResponse(responseCode = "404", description = "사용자 정보 없음"),
          @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      }
  )
  ResponseEntity<Void> softDelete(@Parameter(description = "User ID") @PathVariable String userId);

  @Operation(summary = "사용자 정보 수정")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공", content = @Content(schema = @Schema(implementation = UserDto.class))),
          @ApiResponse(responseCode = "400", description = "잘못된 요청 (입력값 검증 실패)"),
          @ApiResponse(responseCode = "403", description = "사용자 정보 수정 권한 없음"),
          @ApiResponse(responseCode = "404", description = "사용자 정보 없음"),
          @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      }
  )
  ResponseEntity<UserDto> update(
      @Parameter(description = "User ID") @PathVariable String userId,
      @RequestBody UserUpdateRequest request
  );

  @Operation(summary = "파워 유저 목록 조회")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "파워 유저 목록 조회 성공", content = @Content(schema = @Schema(implementation = PageResponse.class))),
          @ApiResponse(responseCode = "400", description = "잘못된 요청 (랭킹 기간 오류, 정렬 방향 오류 등)"),
          @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      }
  )
  ResponseEntity<PageResponse> powerList(PowerUserQueryParams params);

  @Operation(summary = "사용자 물리 삭제")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "204", description = "사용자 삭제 성공"),
          @ApiResponse(responseCode = "403", description = "사용자 삭제 권한 없음"),
          @ApiResponse(responseCode = "404", description = "사용자 정보 없음"),
          @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      }
  )
  ResponseEntity<Void> hardDelete(@Parameter(description = "User ID") @PathVariable String userId);
}
