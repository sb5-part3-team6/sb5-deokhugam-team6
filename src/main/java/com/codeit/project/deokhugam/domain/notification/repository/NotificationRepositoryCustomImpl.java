package com.codeit.project.deokhugam.domain.notification.repository;

import com.codeit.project.deokhugam.domain.notification.entity.Notification;
import com.codeit.project.deokhugam.domain.notification.entity.QNotification;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom {

  private final JPAQueryFactory qf;
  private final QNotification notification = QNotification.notification;
  private static final String NEW_LINE = "[\\r\\n]";

  @Override
  public List<Notification> findNotificationsByUserId(Long userId, String direction, String cursor,
      String after, int limit) {

    BooleanBuilder builder = new BooleanBuilder();
    builder.and(notification.user.id.eq(userId));

    if (cursor != null) {
      builder.and(notification.id.lt(Long.parseLong(cursor)));
    }

    OrderSpecifier<?> order =
        "ASC".equalsIgnoreCase(direction) ? notification.id.asc() : notification.id.desc();

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

  @Override
  public Optional<Notification> findByReviewIdAndTypeAndContentIgnoreNewline(Long reviewId,
      String type, String content) {

    String normalizedContent = content.replaceAll(NEW_LINE, "");

    return qf.selectFrom(notification)
             .where(notification.review.id.eq(reviewId), notification.type.eq(type))
             .fetch()
             .stream()
             .filter(n -> n.getContent()
                           .replaceAll(NEW_LINE, "")
                           .equals(normalizedContent))
             .findFirst();
  }

  public Long deleteByReviewIdAndTypeAndContentIgnoreNewline(Long reviewId, String type,
      String content) {

    List<Notification> targets = qf.selectFrom(notification)
                                   .where(notification.review.id.eq(reviewId),
                                       notification.type.eq(type))
                                   .fetch()
                                   .stream()
                                   .filter(n -> n.getContent()
                                                 .replaceAll(NEW_LINE, "")
                                                 .equals(content.replaceAll(NEW_LINE, "")))
                                   .toList();

    if (!targets.isEmpty()) {
      qf.delete(notification)
        .where(notification.in(targets))
        .execute();
    }
    return (long) targets.size();
  }
}