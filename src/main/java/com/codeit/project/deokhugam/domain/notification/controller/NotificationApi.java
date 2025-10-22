package com.codeit.project.deokhugam.domain.notification.controller;

import com.codeit.project.deokhugam.domain.notification.dto.CursorPageResponseNotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import org.springframework.http.ResponseEntity;

@Tag(name = "알림 관리", description = "알림 관련 API")
public interface NotificationApi {

  @Operation(summary = "알림 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "알림목록 조회 성공", content = @Content(schema = @Schema(implementation = CursorPageResponseNotificationDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 (정렬 방향 오류, 페이지네이션 파라미터 오류, 사용자 ID 누락)"),
      @ApiResponse(responseCode = "404", description = "사용자 정보 없음"),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류")})
  ResponseEntity<CursorPageResponseNotificationDto> getNotifications(String userId,
      String direction, LocalDate cursor, LocalDate after, Integer limit);

  @Operation(summary = "알림 읽음 상태 업데이트")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "알림 상태 업데이트 성공", content = @Content(schema = @Schema(implementation = NotificationDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 (입력값 검증 실패, 요청자 ID 누락)"),
      @ApiResponse(responseCode = "403", description = "알림 수정 권한 없음"),
      @ApiResponse(responseCode = "404", description = "알림 정보 없음"),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류")})
  ResponseEntity<NotificationDto> checkNotificationById(
      @Parameter(description = "Notification ID") String notificationId,
      NotificationUpdateRequest request, @Parameter(description = "User ID") String userId);

  @Operation(summary = "모든 알림 읽음 처리")
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "알림 읽음 처리 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 (요청자 ID 누락)"),
      @ApiResponse(responseCode = "404", description = "알림 정보 없음"),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류")})
  ResponseEntity<Void> checkAllNotification(@Parameter(description = "User ID") String userId);
}
