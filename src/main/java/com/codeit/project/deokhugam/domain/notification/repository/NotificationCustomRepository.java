package com.codeit.project.deokhugam.domain.notification.repository;

import com.codeit.project.deokhugam.domain.notification.entity.Notification;
import java.time.LocalDate;
import java.util.List;

public interface NotificationCustomRepository {
  List<Notification> findNotificationsByUserId(Long userId, String direction, LocalDate cursor, LocalDate after, int limit);
  Long countByUserId(Long userId);
}