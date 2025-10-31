package com.codeit.project.deokhugam.domain.notification.event;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

import com.codeit.project.deokhugam.domain.book.entity.Book;
import com.codeit.project.deokhugam.domain.comment.dto.event.CommentDeleteEvent;
import com.codeit.project.deokhugam.domain.comment.dto.event.CommentEvent;
import com.codeit.project.deokhugam.domain.comment.dto.event.CommentUpdateEvent;
import com.codeit.project.deokhugam.domain.notification.dto.command.NotificationCreateCommand;
import com.codeit.project.deokhugam.domain.notification.dto.command.NotificationDeleteCommand;
import com.codeit.project.deokhugam.domain.notification.dto.command.NotificationUpdateCommand;
import com.codeit.project.deokhugam.domain.notification.entity.NotificationType;
import com.codeit.project.deokhugam.domain.notification.service.NotificationService;
import com.codeit.project.deokhugam.domain.review.dto.event.ReviewLikedDeleteEvent;
import com.codeit.project.deokhugam.domain.review.dto.event.ReviewLikedEvent;
import com.codeit.project.deokhugam.domain.review.dto.event.ReviewRankedEvent;
import com.codeit.project.deokhugam.domain.review.entity.Review;
import com.codeit.project.deokhugam.domain.user.entity.User;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationEventHandlerTest {

  @Mock
  private NotificationService notificationService;

  @InjectMocks
  private NotificationEventHandler eventHandler;

  private User user;
  private Review review;
  private String commentDataOld;
  private String commentDataNew;

  @BeforeEach
  void setUp() {
    user = new User("test@test.com", "tester", "password");
    review = new Review(user,
        new Book("Title", "Author", "Desc", "Publisher", LocalDate.now(), "1234567890", "url"),
        "좋아요", 5);
    commentDataOld = "이전 댓글";
    commentDataNew = "수정된 댓글";
  }

  @Test
  @DisplayName("리뷰 좋아요 이벤트 발생 시 NotificationService.create 호출")
  void handleReviewLikedEvent_callsNotificationServiceCreate() {
    ReviewLikedEvent event = new ReviewLikedEvent(review, user);

    eventHandler.handleReviewLikedEvent(event);

    ArgumentCaptor<NotificationCreateCommand> captor = ArgumentCaptor.forClass(
        NotificationCreateCommand.class);
    verify(notificationService).create(captor.capture());

    NotificationCreateCommand cmd = captor.getValue();
    assertThat(cmd.type()).isEqualTo(NotificationType.REVIEW_LIKED);
    assertThat(cmd.reactor()).isEqualTo(user);
    assertThat(cmd.review()).isEqualTo(review);
  }

  @Test
  @DisplayName("리뷰 댓글 이벤트 발생 시 NotificationService.create 호출")
  void handleReviewCommentedEvent_callsNotificationServiceCreate() {
    CommentEvent event = new CommentEvent(review, user, "새 댓글");

    eventHandler.handleCommentEvent(event);

    ArgumentCaptor<NotificationCreateCommand> captor = ArgumentCaptor.forClass(
        NotificationCreateCommand.class);
    verify(notificationService).create(captor.capture());

    NotificationCreateCommand cmd = captor.getValue();
    assertThat(cmd.type()).isEqualTo(NotificationType.REVIEW_COMMENTED);
    assertThat(cmd.reactor()).isEqualTo(user);
    assertThat(cmd.review()).isEqualTo(review);
    assertThat(cmd.data()).isEqualTo("새 댓글");
  }

  @Test
  @DisplayName("리뷰 랭킹 이벤트 발생 시 NotificationService.create 호출")
  void handleReviewRankedEvent_callsNotificationServiceCreate() {
    ReviewRankedEvent event = new ReviewRankedEvent(review, user, "");

    eventHandler.handleReviewRankedEventEvent(event);

    ArgumentCaptor<NotificationCreateCommand> captor = ArgumentCaptor.forClass(
        NotificationCreateCommand.class);
    verify(notificationService).create(captor.capture());

    NotificationCreateCommand cmd = captor.getValue();
    assertThat(cmd.type()).isEqualTo(NotificationType.REVIEW_RANKED);
    assertThat(cmd.reactor()).isEqualTo(user);
    assertThat(cmd.review()).isEqualTo(review);
    assertThat(cmd.data()).isEqualTo("");
  }

  @Test
  @DisplayName("리뷰 좋아요 삭제 이벤트 발생 시 NotificationService.delete 호출")
  void handleReviewLikedDeleteEvent_callsNotificationServiceDelete() {
    ReviewLikedDeleteEvent event = new ReviewLikedDeleteEvent(review, user, true);

    eventHandler.handleReviewLikedDeleteEvent(event);

    ArgumentCaptor<NotificationDeleteCommand> captor = ArgumentCaptor.forClass(
        NotificationDeleteCommand.class);
    verify(notificationService).delete(captor.capture());

    NotificationDeleteCommand cmd = captor.getValue();
    assertThat(cmd.type()).isEqualTo(NotificationType.REVIEW_LIKED);
    assertThat(cmd.reactor()).isEqualTo(user);
    assertThat(cmd.review()).isEqualTo(review);
  }

  @Test
  @DisplayName("리뷰 댓글 삭제 이벤트 발생 시 NotificationService.delete 호출")
  void handleReviewCommentedDeleteEvent_callsNotificationServiceDelete() {
    CommentDeleteEvent event = new CommentDeleteEvent(review, user, "삭제 댓글", true);

    eventHandler.handleCommentDeleteEvent(event);

    ArgumentCaptor<NotificationDeleteCommand> captor = ArgumentCaptor.forClass(
        NotificationDeleteCommand.class);
    verify(notificationService).delete(captor.capture());

    NotificationDeleteCommand cmd = captor.getValue();
    assertThat(cmd.type()).isEqualTo(NotificationType.REVIEW_COMMENTED);
    assertThat(cmd.reactor()).isEqualTo(user);
    assertThat(cmd.review()).isEqualTo(review);
    assertThat(cmd.data()).isEqualTo("삭제 댓글");
  }

  @Test
  @DisplayName("댓글 수정 이벤트 발생 시 NotificationService.update 호출")
  void handleCommentedUpdateEvent_callsNotificationServiceUpdate() {
    CommentUpdateEvent event = new CommentUpdateEvent(review, user, commentDataOld, commentDataNew);

    eventHandler.handleCommentUpdateEvent(event);

    ArgumentCaptor<NotificationUpdateCommand> captor = ArgumentCaptor.forClass(
        NotificationUpdateCommand.class);
    verify(notificationService).update(captor.capture());

    NotificationUpdateCommand cmd = captor.getValue();
    assertThat(cmd.user()).isEqualTo(user);
    assertThat(cmd.review()).isEqualTo(review);
    assertThat(cmd.oldData()).isEqualTo(commentDataOld);
    assertThat(cmd.newData()).isEqualTo(commentDataNew);
  }
}