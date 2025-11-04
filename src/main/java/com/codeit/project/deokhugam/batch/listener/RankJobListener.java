package com.codeit.project.deokhugam.batch.listener;

import com.codeit.project.deokhugam.batch.service.BatchMetricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankJobListener implements JobExecutionListener {

    private final BatchMetricsService batchMetricsService;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info(" [RankJob] 배치 Job 시작: {}", jobExecution.getJobInstance().getJobName());
        batchMetricsService.incrementJobRun();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info(" [RankJob] 배치 Job 성공: {}", jobExecution.getJobInstance().getJobName());
            batchMetricsService.incrementJobSuccess();
        } else {
            log.error(" [RankJob] 배치 Job 실패: {} - 상태: {}",
                    jobExecution.getJobInstance().getJobName(),
                    jobExecution.getStatus());
            batchMetricsService.incrementJobFail();
        }
    }
}
