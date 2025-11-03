package com.codeit.project.deokhugam.domain.user.service;

import com.codeit.project.deokhugam.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserBatchServiceImpl implements UserBatchService {

  private final UserRepository userRepository;

  @Override
  public void cleanBatch() {
    Long cnt = userRepository.deleteExpiredSoftDeletedUsers();
    log.info("deleted {} expired soft deleted users", cnt);
  }
}