package com.codeit.project.deokhugam.batch.schedular;

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
public class BookBatchSchedular {

  private final JobLauncher jobLauncher;
  private final Job dailyBookStatJob;
  private final Job weeklyBookStatJob;
  private final Job monthlyBookStatJob;
  private final Job allTimeBookStatJob;

//  @Scheduled(cron = "0 0 * * * ?", zone = "Asia/Seoul")
  @Scheduled(initialDelay = 1000, fixedDelay = 1000 * 60 * 60)
  public void runAllJobs() {
    runJob(monthlyBookStatJob, "monthlyBook");
    runJob(dailyBookStatJob, "dailyBook");
    runJob(weeklyBookStatJob, "weeklyBook");
    runJob(allTimeBookStatJob, "allTimeBook");
  }

  private void runJob(Job job, String name) {
    try {
      JobParameters jobParameters = new JobParametersBuilder()
          .addString("requestDate", String.valueOf(System.currentTimeMillis()))
          .addString("jobName", name)
          .toJobParameters();

      jobLauncher.run(job, jobParameters);
      log.info("{} batch job 이 성공적으로 실행되었습니다.", name);

    } catch (Exception e) {
      log.error("{} batch job 실패", name, e);
    }
  }
}