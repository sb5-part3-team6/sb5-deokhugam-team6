package com.codeit.project.deokhugam.domain.notification.event;

import com.codeit.project.deokhugam.domain.notification.dto.command.NotificationCreateCommand;
import com.codeit.project.deokhugam.domain.notification.entity.NotificationType;
import com.codeit.project.deokhugam.domain.notification.service.NotificationService;
import com.codeit.project.deokhugam.domain.review.dto.event.ReviewCommentedEvent;
import com.codeit.project.deokhugam.domain.review.dto.event.ReviewLikedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class ReviewEventHandler {

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
    public void handleReviewCommentedEvent(ReviewCommentedEvent e) {
        notificationService.create(NotificationCreateCommand.builder()
                .type(NotificationType.REVIEW_COMMENTED)
                .reactor(e.user())
                .review(e.review())
                .build());
    }
}
