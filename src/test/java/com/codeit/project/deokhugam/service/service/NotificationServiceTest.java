package com.codeit.project.deokhugam.service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.codeit.project.deokhugam.domain.book.entity.Book;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationCreateCommand;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.NotificationUpdateRequest;
import com.codeit.project.deokhugam.domain.notification.entity.Notification;
import com.codeit.project.deokhugam.domain.notification.entity.NotificationType;
import com.codeit.project.deokhugam.domain.notification.exception.NotificationInvalidUserException;
import com.codeit.project.deokhugam.domain.notification.exception.NotificationNotFoundException;
import com.codeit.project.deokhugam.domain.notification.mapper.NotificationMapper;
import com.codeit.project.deokhugam.domain.notification.repository.NotificationRepository;
import com.codeit.project.deokhugam.domain.notification.service.NotificationServiceImpl;
import com.codeit.project.deokhugam.domain.review.entity.Review;
import com.codeit.project.deokhugam.domain.user.entity.User;
import com.codeit.project.deokhugam.domain.user.repository.UserRepository;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

  @Mock
  private NotificationRepository notificationRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private NotificationMapper notificationMapper;

  @InjectMocks
  private NotificationServiceImpl notificationService;

  private Notification notification;
  private NotificationDto notificationDto;
  private User user;
  private User reviewOwner;
  private Review review;

  @BeforeEach
  void setUp() {
    user = new User("testUser", "", "");
    ReflectionTestUtils.setField(user, "id", 1L);

    reviewOwner = new User("owner@test.com", "ownerNick", "pw1234");
    ReflectionTestUtils.setField(reviewOwner, "id", 2L);
    review = new Review(reviewOwner, new Book(), "리뷰 내용", 5);
    ReflectionTestUtils.setField(review, "id", 1L);

    notification = new Notification(new Review(), user, "", "", false);
    ReflectionTestUtils.setField(notification, "id", 1L);

    notificationDto = new NotificationDto("1", "1", "1", "Test Notification", "", false,
        LocalDateTime.now(), LocalDateTime.now());
  }

  @Test
  @DisplayName("단일 알림 확인 상태 업데이트 성공")
  void updateNotificationById_Success() {
    given(notificationRepository.findById(1L)).willReturn(Optional.of(notification));
    given(notificationMapper.toDto(notification)).willReturn(notificationDto);

    NotificationDto result = notificationService.checkById("1", new NotificationUpdateRequest(true),
        "1");

    assertThat(notification.getConfirmed()).isTrue();
    assertThat(result).isEqualTo(notificationDto);
    verify(notificationRepository).findById(1L);
    verify(notificationMapper).toDto(notification);
  }

  @Test
  @DisplayName("단일 알림 확인 상태 업데이트 - 알림 없음")
  void updateNotificationById_NotFound() {
    given(notificationRepository.findById(1L)).willReturn(Optional.empty());

    assertThatThrownBy(() -> notificationService.checkById("1", new NotificationUpdateRequest(true),
        "2")).isInstanceOf(NotificationNotFoundException.class);
  }

  @Test
  @DisplayName("단일 알림 확인 상태 업데이트 - 잘못된 유저")
  void updateNotificationById_InvalidUser() {
    given(notificationRepository.findById(1L)).willReturn(Optional.of(notification));

    assertThatThrownBy(() -> notificationService.checkById("1", new NotificationUpdateRequest(true),
        "2")).isInstanceOf(NotificationInvalidUserException.class);
  }

  @Test
  @DisplayName("모든 알림 확인 상태 업데이트 성공")
  void updateNotificationAll_Success() {
    Notification notification2 = new Notification(new Review(), user, "", "", false);
    ReflectionTestUtils.setField(notification2, "id", 2L);

    given(userRepository.existsById(1L)).willReturn(true);
    given(notificationRepository.findAllByUserIdAndConfirmedFalse(1L)).willReturn(
        List.of(notification, notification2));

    notificationService.checkAll("1");

    assertThat(notification.getConfirmed()).isTrue();
    verify(notificationRepository).findAllByUserIdAndConfirmedFalse(1L);
  }

  @Test
  @DisplayName("알림 조회 - getNotifications 호출")
  void getNotifications_ReturnsNull() {
    LocalDate after = LocalDate.now()
                               .minusDays(1);
    PageResponse result = notificationService.getByCursor("1", "DESC", after, after, 20);

    assertThat(result).isNotNull();
  }

  @Test
  @DisplayName("리뷰 좋아요 알림 생성 성공")
  void createReviewLikedNotification_success() {
    NotificationCreateCommand command = NotificationCreateCommand.builder()
                                                                 .reviewOwner(reviewOwner)
                                                                 .review(review)
                                                                 .reactor(user)
                                                                 .type(
                                                                     NotificationType.REVIEW_LIKED)
                                                                 .build();

    notificationService.create(command);

    ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
    verify(notificationRepository).save(captor.capture());

    Notification saved = captor.getValue();
    assertThat(saved.getUser()).isEqualTo(reviewOwner);
    assertThat(saved.getReview()).isEqualTo(review);
    assertThat(saved.getType()).isEqualTo(NotificationType.REVIEW_LIKED.name());
    assertThat(saved.getContent()).contains(user.getNickname());
    assertThat(saved.getConfirmed()).isFalse();
  }

  @Test
  @DisplayName("리뷰 댓글 알림 생성 성공")
  void createReviewCommentedNotification_success() {
    NotificationCreateCommand command = NotificationCreateCommand.builder()
                                                                 .reviewOwner(reviewOwner)
                                                                 .review(review)
                                                                 .reactor(user)
                                                                 .type(
                                                                     NotificationType.REVIEW_COMMENTED)
                                                                 .build();

    notificationService.create(command);

    ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
    verify(notificationRepository).save(captor.capture());

    Notification saved = captor.getValue();
    assertThat(saved.getContent()).contains(user.getNickname());
    assertThat(saved.getType()).isEqualTo(NotificationType.REVIEW_COMMENTED.name());
  }

  @Test
  @DisplayName("리뷰 랭킹 진입 알림 생성 성공")
  void createReviewRankedNotification_success() {
    NotificationCreateCommand command = NotificationCreateCommand.builder()
                                                                 .reviewOwner(reviewOwner)
                                                                 .review(review)
                                                                 .rank(3)
                                                                 .type(
                                                                     NotificationType.REVIEW_RANKED)
                                                                 .build();

    notificationService.create(command);

    ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
    verify(notificationRepository).save(captor.capture());

    Notification saved = captor.getValue();
    assertThat(saved.getContent()).contains("3위");
    assertThat(saved.getType()).isEqualTo(NotificationType.REVIEW_RANKED.name());
  }
}