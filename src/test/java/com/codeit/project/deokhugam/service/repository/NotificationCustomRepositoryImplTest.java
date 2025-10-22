package com.codeit.project.deokhugam.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.codeit.project.deokhugam.domain.book.entity.Book;
import com.codeit.project.deokhugam.domain.notification.entity.Notification;
import com.codeit.project.deokhugam.domain.notification.repository.NotificationRepository;
import com.codeit.project.deokhugam.domain.review.entity.Review;
import com.codeit.project.deokhugam.domain.user.entity.User;
import com.codeit.project.deokhugam.global.config.QuerydslConfig;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QuerydslConfig.class)
class NotificationCustomRepositoryImplTest {

  @Autowired
  private EntityManager em;

  @Autowired
  private NotificationRepository notificationRepository;

  @Autowired
  private com.codeit.project.deokhugam.domain.notification.repository.NotificationCustomRepositoryImpl notificationRepositoryImpl;

  private User user1;
  private User user2;
  private Book book;
  private Review review;

  @BeforeEach
  void setUp() {
    user1 = new User("user1@test.com", "user1", "pw1234");
    user2 = new User("user2@test.com", "user2", "pw5678");
    em.persist(user1);
    em.persist(user2);

    book = new Book(
        "Clean Code", "Robert C. Martin", "클린 코드 설명",
        "인사이트", LocalDate.of(2008, 8, 1),
        "9788966260959", "https://example.com/cleancode.jpg"
    );
    em.persist(book);

    review = new Review(user1, book, "좋은 책이에요", 5);
    em.persist(review);

    for (int i = 1; i <= 30; i++) {
      Notification n = new Notification(
          review,
          user1,
          i % 2 == 0 ? "LIKE" : "COMMENT",
          "알림 메시지 " + i,
          i % 2 == 0
      );
      em.persist(n);
    }

    for (int i = 1; i <= 5; i++) {
      Notification n = new Notification(
          review,
          user2,
          "LIKE",
          "user2 알림 " + i,
          false
      );
      em.persist(n);
    }

    em.flush();
    em.clear();
  }

  @Test
  @DisplayName("JPA - findAllByUserId() 기본 조회")
  void findAllByUserId() {
    List<Notification> result = notificationRepository.findAllByUserIdAndConfirmedFalse(user1.getId());
    assertThat(result).hasSize(15)
                      .allMatch(n -> n.getUser()
                                      .getId()
                                      .equals(user1.getId()));
  }

  @Test
  @DisplayName("QueryDSL - DESC 정렬, 전체 조회")
  void findNotificationsByUserId_desc() {
    List<Notification> result = notificationRepositoryImpl.findNotificationsByUserId(
        user1.getId(), "DESC", null, null, 50
    );

    assertThat(result).hasSize(30);
    assertThat(result.get(0)
                     .getCreatedAt())
        .isAfterOrEqualTo(result.get(1)
                                .getCreatedAt());
  }

  @Test
  @DisplayName("QueryDSL - ASC 정렬 확인")
  void findNotificationsByUserId_asc() {
    List<Notification> result = notificationRepositoryImpl.findNotificationsByUserId(
        user1.getId(), "ASC", null, null, 50
    );

    for (int i = 1; i < result.size(); i++) {
      assertThat(result.get(i - 1)
                       .getCreatedAt())
          .isBeforeOrEqualTo(result.get(i)
                                   .getCreatedAt());
    }
  }

  @Test
  @DisplayName("QueryDSL - confirmed 필터 확인")
  void findNotificationsByUserId_confirmed() {
    List<Notification> result = notificationRepositoryImpl.findNotificationsByUserId(
        user1.getId(), "DESC", null, null, 50
    );

    boolean hasUnconfirmed = result.stream()
                                   .anyMatch(n -> !n.getConfirmed());
    boolean hasConfirmed = result.stream()
                                 .anyMatch(Notification::getConfirmed);

    assertThat(hasUnconfirmed).isTrue();
    assertThat(hasConfirmed).isTrue();
  }

  @Test
  @DisplayName("QueryDSL - cursor 기반 페이징 확인")
  void findNotificationsByUserId_cursor() {
    List<Notification> all = notificationRepositoryImpl.findNotificationsByUserId(
        user1.getId(), "DESC", null, null, 50
    );

    Notification cursorNotification = all.get(14);

    List<Notification> afterCursor = notificationRepositoryImpl.findNotificationsByUserId(
        user1.getId(), "DESC", cursorNotification.getCreatedAt()
                                                 .toLocalDate(), null, 50
    );

    for (Notification n : afterCursor) {
      assertThat(n.getCreatedAt()).isBefore(cursorNotification.getCreatedAt());
    }
  }

  @Test
  @DisplayName("QueryDSL - after(LocalDate) 필터 확인")
  void findNotificationsByUserId_after() {
    LocalDate after = LocalDate.now()
                               .plusDays(1);

    List<Notification> result = notificationRepositoryImpl.findNotificationsByUserId(
        user1.getId(), "DESC", null, after, 50
    );

    assertThat(result).isEmpty();
  }
}