package com.codeit.project.deokhugam.domain.notification.repository;

import com.codeit.project.deokhugam.domain.notification.entity.Notification;
import com.codeit.project.deokhugam.domain.notification.entity.QNotification;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationCustomRepositoryImpl implements NotificationCustomRepository {

  private final JPAQueryFactory qf;
  private final QNotification notification = QNotification.notification;

  @Override
  public List<Notification> findNotificationsByUserId(Long userId, String direction,
      LocalDate cursor, LocalDate after, int limit) {

    BooleanBuilder builder = new BooleanBuilder();
    builder.and(notification.user.id.eq(userId));

    if (cursor != null) {
      builder.and(notification.createdAt.lt(cursor.atStartOfDay()));
    }

    if (after != null && cursor != after) {
      // TODO : 프로토 타입 cursor = after던데 체크 필요
      builder.and(notification.createdAt.gt(after.atStartOfDay()));
    }

    OrderSpecifier<?> order = "ASC".equalsIgnoreCase(direction) ? notification.createdAt.asc()
        : notification.createdAt.desc();

    return qf.selectFrom(notification)
             .where(builder)
             .orderBy(order)
             .limit(limit)
             .fetch();
  }

  @Override
  public Long countByUserId(Long userId) {
    return qf.select(notification.count())
             .from(notification)
             .where(notification.user.id.eq(userId))
             .fetchOne();
  }
}