package com.codeit.project.deokhugam.batch.scheduler;

import com.codeit.project.deokhugam.domain.rank.entity.RankTarget;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankBatchScheduler {
  private final JobLauncher jobLauncher;
  private final Job statJob;

  @Scheduled(cron = "0 5 0 * * *", zone = "Asia/Seoul")
//  @Scheduled(initialDelay = 1000, fixedDelay = 1000 * 60 * 60)
  public void runAllStats() {
    runJob(RankTarget.BOOK, RankType.WEEKLY);
    runJob(RankTarget.BOOK, RankType.MONTHLY);
    runJob(RankTarget.BOOK, RankType.ALL_TIME);

    runJob(RankTarget.REVIEW, RankType.WEEKLY);
    runJob(RankTarget.REVIEW, RankType.MONTHLY);
    runJob(RankTarget.REVIEW, RankType.ALL_TIME);

    runJob(RankTarget.USER, RankType.WEEKLY);
    runJob(RankTarget.USER, RankType.MONTHLY);
    runJob(RankTarget.USER, RankType.ALL_TIME);
  }

  @Scheduled(initialDelay = 1000, fixedDelay = 1000 * 60 * 60)
  public void runDaily(){
    runJob(RankTarget.BOOK, RankType.DAILY);
    runJob(RankTarget.REVIEW, RankType.DAILY);
    runJob(RankTarget.USER, RankType.DAILY);
  }

  private void runJob(RankTarget target, RankType type) {
    try {
      JobParameters jobParameters = new JobParametersBuilder()
          .addString("requestDate", String.valueOf(System.currentTimeMillis()))
          .addString("rankTarget", target.name())
          .addString("rankType", type.name())
          .toJobParameters();

      jobLauncher.run(statJob, jobParameters);
      log.info("{} {} batch job success", target, type);

    } catch (Exception e) {
      log.error("{} {} batch job fail", target, type, e);
    }
  }
}
