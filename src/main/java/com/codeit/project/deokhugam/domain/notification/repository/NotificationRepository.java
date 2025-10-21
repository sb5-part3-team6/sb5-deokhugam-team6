package com.codeit.project.deokhugam.domain.notification.repository;

import com.codeit.project.deokhugam.domain.notification.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findAllByUserId(Long userId);
}
