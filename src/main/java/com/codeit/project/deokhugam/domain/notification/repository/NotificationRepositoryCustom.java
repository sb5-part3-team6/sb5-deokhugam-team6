package com.codeit.project.deokhugam.domain.notification.repository;

import com.codeit.project.deokhugam.domain.notification.entity.Notification;
import java.util.List;

public interface NotificationRepositoryCustom {
  List<Notification> findNotificationsByUserId(Long userId, String direction, String cursor, String after, int limit);
  Long countByUserId(Long userId);
  Long deleteConfirmedOlderThanOneWeek();
}