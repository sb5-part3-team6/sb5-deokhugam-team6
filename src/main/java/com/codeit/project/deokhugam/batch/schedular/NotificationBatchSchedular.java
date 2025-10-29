package com.codeit.project.deokhugam.batch.schedular;

import com.codeit.project.deokhugam.domain.notification.service.NotificationBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationBatchSchedular {

  private final NotificationBatchService notificationBatchService;

  @Scheduled(cron = "0 10 0 * * *")
  public void cleanBatch() {
    notificationBatchService.cleanBatch();
  }
}