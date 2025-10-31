package com.codeit.project.deokhugam.domain.notification.event;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class NotificationEventHandler {

  private final NotificationService notificationService;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleReviewLikedEvent(ReviewLikedEvent e) {
    notificationService.create(NotificationCreateCommand.builder()
                                                        .type(NotificationType.REVIEW_LIKED)
                                                        .reactor(e.user())
                                                        .review(e.review())
                                                        .build());
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleCommentEvent(CommentEvent e) {
    notificationService.create(NotificationCreateCommand.builder()
                                                        .type(NotificationType.REVIEW_COMMENTED)
                                                        .reactor(e.user())
                                                        .review(e.review())
                                                        .data(e.data())
                                                        .build());
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleReviewLikedDeleteEvent(ReviewLikedDeleteEvent e) {
    notificationService.delete(NotificationDeleteCommand.builder()
                                                        .type(NotificationType.REVIEW_LIKED)
                                                        .reactor(e.user())
                                                        .review(e.review())
                                                        .only(e.only())
                                                        .build());
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleCommentDeleteEvent(CommentDeleteEvent e) {
    notificationService.delete(NotificationDeleteCommand.builder()
                                                        .type(NotificationType.REVIEW_COMMENTED)
                                                        .reactor(e.user())
                                                        .review(e.review())
                                                        .data(e.data())
                                                        .only(e.only())
                                                        .build());
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleCommentUpdateEvent(CommentUpdateEvent e) {
    notificationService.update(NotificationUpdateCommand.builder()
                                                        .review(e.review())
                                                        .user(e.user())
                                                        .oldData(e.oldData())
                                                        .newData(e.newData())
                                                        .build());
  }
}