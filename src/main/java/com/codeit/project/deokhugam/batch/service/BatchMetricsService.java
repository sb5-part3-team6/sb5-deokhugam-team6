package com.codeit.project.deokhugam.batch.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class BatchMetricsService {

    private final Counter jobRunCounter;
    private final Counter jobSuccessCounter;
    private final Counter jobFailedCounter;
    private final Counter itemProcessedCounter;

    public BatchMetricsService(MeterRegistry meterRegistry) {
        this.jobRunCounter = Counter.builder("batch_job_runs_total")
                .description("총 배치 Job 실행 횟수")
                .register(meterRegistry);
        this.jobSuccessCounter = Counter.builder("batch_job_success_total")
                .description("성공한 배치 Job 수")
                .register(meterRegistry);
        this.jobFailedCounter = Counter.builder("batch_job_fail_total")
                .description("실패한 배치 Job 수")
                .register(meterRegistry);
        this.itemProcessedCounter = Counter.builder("batch_items_processed_total")
                .description("처리된 아이템 수")
                .register(meterRegistry);
    }
    public void incrementJobRun() {
        jobRunCounter.increment();
    }
    public void incrementJobSuccess() {
        jobSuccessCounter.increment();
    }
    public void incrementJobFail() {
        jobFailedCounter.increment();
    }
    public void incrementItemsProcessed(long count) {
        itemProcessedCounter.increment(count);
    }
}
