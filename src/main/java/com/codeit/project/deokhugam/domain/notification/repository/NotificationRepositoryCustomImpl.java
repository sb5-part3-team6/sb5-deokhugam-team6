package com.codeit.project.deokhugam.domain.notification.repository;

import com.codeit.project.deokhugam.domain.notification.entity.Notification;
import com.codeit.project.deokhugam.domain.notification.entity.QNotification;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom {

  private final JPAQueryFactory qf;
  private final QNotification notification = QNotification.notification;

  @Override
  public List<Notification> findNotificationsByUserId(Long userId, String direction,
      String cursor, String after, int limit) {

    BooleanBuilder builder = new BooleanBuilder();
    builder.and(notification.user.id.eq(userId));

    if (cursor != null) {
      builder.and(notification.id.lt(Long.parseLong(cursor)));
    }

    OrderSpecifier<?> order = "ASC".equalsIgnoreCase(direction) ? notification.id.asc()
        : notification.id.desc();

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

  @Override
  public Long deleteConfirmedOlderThanOneWeek() {
    LocalDateTime oneWeekAgo = LocalDateTime.now()
                                            .minusWeeks(1);

    return qf.delete(notification)
             .where(notification.confirmed.isTrue(), notification.createdAt.loe(oneWeekAgo))
             .execute();
  }
}