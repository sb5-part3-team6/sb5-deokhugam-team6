package com.codeit.project.deokhugam.domain.notification.repository;

import com.codeit.project.deokhugam.domain.notification.entity.Notification;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NotificationRepositoryCustom {

  List<Notification> findNotificationsByUserId(Long userId, String direction, String cursor,
      String after, int limit);

  Long countByUserId(Long userId);

  Long deleteConfirmedOlderThanOneWeek();

  Optional<Notification> findByReviewIdAndTypeAndContentIgnoreNewline(Long reviewId, String type,
      String content);

  Long deleteByReviewIdAndTypeAndContentIgnoreNewline(Long reviewId, String type,
      String content);

  Long deleteByDateAndType(LocalDate date, String type);
}