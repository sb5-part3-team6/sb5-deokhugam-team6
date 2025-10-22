package com.codeit.project.deokhugam.domain.notification.controller;

import com.codeit.project.deokhugam.domain.notification.dto.CursorPageResponseNotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationUpdateRequest;
import com.codeit.project.deokhugam.domain.notification.service.NotificationService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController implements NotificationApi {

  private final NotificationService notificationService;

  @Override
  @GetMapping
  public ResponseEntity<CursorPageResponseNotificationDto> getNotifications(
      @RequestParam String userId,
      @RequestParam(name = "direction", defaultValue = "DESC") String direction,
      @RequestParam String cursor,
      @RequestParam(name = "after", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate after,
      @RequestParam(name = "limit", defaultValue = "20") Integer limit) {

    CursorPageResponseNotificationDto page = notificationService.getNotifications(userId, direction,
        cursor, after, limit);

    return ResponseEntity.status(HttpStatus.OK)
                         .body(page);
  }

  @Override
  @PatchMapping("/{notificationId}")
  public ResponseEntity<NotificationDto> checkNotificationById(
      @PathVariable("notificationId") String notificationId,
      @RequestBody NotificationUpdateRequest request,
      @RequestHeader("Deokhugam-Request-User-ID") String userId) {

    NotificationDto notificationDto = notificationService.checkNotificationById(notificationId,
        request, userId);

    return ResponseEntity.status(HttpStatus.OK)
                         .body(notificationDto);
  }

  @Override
  @PatchMapping("/read-all")
  public ResponseEntity<Void> checkAllNotification(
      @RequestHeader("Deokhugam-Request-User-ID") String userId) {
    notificationService.checkAllNotification(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
                         .build();
  }
}
