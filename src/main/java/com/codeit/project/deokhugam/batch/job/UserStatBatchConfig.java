package com.codeit.project.deokhugam.batch.job;

import com.codeit.project.deokhugam.batch.processor.UserStatProcessor;
import com.codeit.project.deokhugam.batch.reader.UserStatReader;
import com.codeit.project.deokhugam.batch.writer.StatWriter;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.entity.RankTarget;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import com.codeit.project.deokhugam.domain.user.dto.UserStatDto;
import com.codeit.project.deokhugam.domain.user.repository.UserRepository;
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
public class UserStatBatchConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final UserRepository userRepository;
  private final StatWriter statWriter;

  @Bean
  public Step monthlyUserStatStep() {
    return new StepBuilder("monthlyUserStatStep", jobRepository)
        .<UserStatDto, Rank>chunk(100, transactionManager)
        .reader(new UserStatReader(userRepository, RankType.MONTHLY))
        .processor(new UserStatProcessor(RankType.MONTHLY, RankTarget.USER))
        .writer(statWriter)
        .build();
  }


  @Bean
  public Job monthlyUserStatJob() {
    return new JobBuilder("monthlyUserStatJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(monthlyUserStatStep())
        .build();
  }
}