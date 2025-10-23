package com.codeit.project.deokhugam.domain.notification.service;

import com.codeit.project.deokhugam.domain.notification.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationBatchServiceImpl implements NotificationBatchService {

  private final NotificationRepository notificationRepository;

  @Override
  @Transactional
  public void cleanBatch() {
    log.info("알림 배치 동작 완료");
  }
}