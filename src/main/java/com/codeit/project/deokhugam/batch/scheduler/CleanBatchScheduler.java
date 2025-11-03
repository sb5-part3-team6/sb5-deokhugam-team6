package com.codeit.project.deokhugam.batch.scheduler;

import com.codeit.project.deokhugam.domain.notification.service.NotificationBatchService;
import com.codeit.project.deokhugam.domain.user.service.UserBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CleanBatchScheduler {

  private final NotificationBatchService notificationBatchService;
  private final UserBatchService userBatchService;

  @Scheduled(cron = "0 10 0 * * *")
  public void cleanBatch() {
    notificationBatchService.cleanBatch();
    userBatchService.cleanBatch();
  }
}