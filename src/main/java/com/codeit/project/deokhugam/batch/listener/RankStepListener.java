package com.codeit.project.deokhugam.batch.listener;

import com.codeit.project.deokhugam.batch.service.BatchMetricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankStepListener implements StepExecutionListener {

    private final BatchMetricsService batchMetricsService;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info(" Step 시작: {}", stepExecution.getStepName());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        long writeCount = stepExecution.getWriteCount();
        log.info(" Step 종료: {}, 처리된 아이템 수: {}",
                stepExecution.getStepName(), writeCount);
        batchMetricsService.incrementItemsProcessed(writeCount);
        return stepExecution.getExitStatus();
    }
}
