package com.codeit.project.deokhugam.service.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.codeit.project.deokhugam.domain.notification.service.NotificationService;
import com.codeit.project.deokhugam.domain.notification.service.NotificationServiceImpl;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NotificationServiceTest {

  private NotificationService notificationService;

  @BeforeEach
  void setUp() {
    notificationService = new NotificationServiceImpl();
  }

  @Test
  @DisplayName("getNotifications 타입 검증")
  void testGetNotificationsType() {
    Object result = notificationService.getNotifications(
        "user1", "DESC", "cursor123", LocalDate.now(), 10
    );

    assertNotNull(result);
  }

  @Test
  @DisplayName("updateNotificationById 타입 검증")
  void testUpdateNotificationByIdType() {
    Object result = notificationService.updateNotificationById("notif1", "user1");

    assertNotNull(result);
  }
}
