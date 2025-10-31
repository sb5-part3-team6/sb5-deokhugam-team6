package com.codeit.project.deokhugam.domain.notification.repository;

import com.codeit.project.deokhugam.domain.notification.entity.Notification;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>,
    NotificationRepositoryCustom {

  List<Notification> findAllByUserIdAndConfirmedFalse(Long userId);

  Long deleteNotificationsByReviewIdAndType(Long reviewId, String type);
}