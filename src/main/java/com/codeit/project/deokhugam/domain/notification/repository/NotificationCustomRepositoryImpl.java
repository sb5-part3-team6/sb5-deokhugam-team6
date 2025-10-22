package com.codeit.project.deokhugam.domain.notification.repository;

import com.codeit.project.deokhugam.domain.notification.entity.Notification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationCustomRepositoryImpl implements NotificationCustomRepository {

  private final JPAQueryFactory qf;

  @Override
  public List<Notification> findNotificationsByUserId(Long userId, String direction, LocalDate cursor,
      LocalDate after, int limit) {
    return List.of();
  }

  @Override
  public Long countByUserId(Long userId) {
    return 0L;
  }
}