package com.codeit.project.deokhugam.batch.scheduler;

import com.codeit.project.deokhugam.domain.rank.entity.RankTarget;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
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
  private final MeterRegistry meterRegistry;
  private Counter batchSuccessCounter;
  private Counter batchFailCounter;

  @PostConstruct
  public void initMetrics() {
    batchSuccessCounter = Counter.builder("batch_jobs_success_total")
            .description("성공적으로 실행한 총 batch job 수")
            .register(meterRegistry);

    batchFailCounter = Counter.builder("batch_jobs_failed_total")
            .description("실패한 총 batch job 수")
            .register(meterRegistry);
  }

//  @Scheduled(cron = "0 */5 * * * *", zone = "Asia/Seoul") // 5분마다 테스트
  @Scheduled(cron = "0 5 0 * * *", zone = "Asia/Seoul")
  public void runAllStats() {
    runJob(RankTarget.BOOK, RankType.DAILY);
    runJob(RankTarget.REVIEW, RankType.DAILY);
    runJob(RankTarget.USER, RankType.DAILY);

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

//  @Scheduled(initialDelay = 1000, fixedDelay = 1000 * 60 * 60)
  public void runDaily(){
    // TODO : 일간 랭킹 애들은 계속 조금씩 갱신할지 or 그냥 아예 어제일자 랭킹을 보여줄지
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
      log.info("{} {} batch job 실행 완료", target, type);
      batchSuccessCounter.increment();

    } catch (Exception e) {
      log.error("{} {} batch job 실행 실패", target, type, e);
      batchFailCounter.increment();
    }
  }
}
