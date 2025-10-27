package com.codeit.project.deokhugam.batch.job;

import com.codeit.project.deokhugam.batch.processor.RankProcessor;
import com.codeit.project.deokhugam.batch.reader.RankReader;
import com.codeit.project.deokhugam.batch.writer.RankWriter;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class RankBatchConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final RankReader statReader;
  private final RankProcessor statProcessor;
  private final RankWriter statWriter;

  @Bean
  public Step statStep() {
    return new StepBuilder("statStep", jobRepository)
        .<Object, Rank>chunk(100, transactionManager)
        .reader(statReader)
        .processor(statProcessor)
        .writer(statWriter)
        .build();
  }

  @Bean
  public Job statJob() {
    return new JobBuilder("statJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(statStep())
        .build();
  }
}