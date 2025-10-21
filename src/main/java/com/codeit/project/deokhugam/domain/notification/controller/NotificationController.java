package com.codeit.project.deokhugam.domain.notification.controller;

import com.codeit.project.deokhugam.domain.notification.dto.CursorPageResponseNotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationUpdateRequest;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
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
public class NotificationController {

  @GetMapping
  public ResponseEntity<CursorPageResponseNotificationDto> getNotifications(
      @RequestParam String userId,
      @RequestParam(name = "direction", defaultValue = "DESC") String direction,
      @RequestParam String cursor,
      @RequestParam(name = "after", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate after,
      @RequestParam(name = "limit", defaultValue = "20") Integer limit) {
    return null;
  }

  @PatchMapping("/{notificationId}")
  public ResponseEntity<NotificationDto> checkNotificationById(
      @PathVariable("notificationId") String notificationId,
      @RequestBody NotificationUpdateRequest request,
      @RequestHeader("Deokhugam-Request-User-ID") String userId) {
    return null;
  }

  @PatchMapping("/read-all")
  public ResponseEntity<NotificationDto> checkAllNotification(
      @RequestHeader("Deokhugam-Request-User-ID") String userId) {
    return null;
  }
}
