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
  public Step dailyUserStatStep() {
    return new StepBuilder("dailyUserStatStep", jobRepository)
        .<UserStatDto, Rank>chunk(100, transactionManager)
        .reader(new UserStatReader(userRepository, RankType.DAILY))
        .processor(new UserStatProcessor(RankType.DAILY, RankTarget.USER))
        .writer(statWriter)
        .build();
  }

  @Bean
  public Step weeklyUserStatStep() {
    return new StepBuilder("weeklyUserStatStep", jobRepository)
        .<UserStatDto, Rank>chunk(100, transactionManager)
        .reader(new UserStatReader(userRepository, RankType.WEEKLY))
        .processor(new UserStatProcessor(RankType.WEEKLY, RankTarget.USER))
        .writer(statWriter)
        .build();
  }

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
  public Step allTimeUserStatStep() {
    return new StepBuilder("allTimeUserStatStep", jobRepository)
        .<UserStatDto, Rank>chunk(100, transactionManager)
        .reader(new UserStatReader(userRepository, RankType.ALL_TIME))
        .processor(new UserStatProcessor(RankType.ALL_TIME, RankTarget.USER))
        .writer(statWriter)
        .build();
  }

  @Bean
  public Job dailyUserStatJob() {
    return new JobBuilder("dailyUserStatJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(dailyUserStatStep())
        .build();
  }

  @Bean
  public Job weeklyUserStatJob() {
    return new JobBuilder("weeklyUserStatJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(weeklyUserStatStep())
        .build();
  }

  @Bean
  public Job monthlyUserStatJob() {
    return new JobBuilder("monthlyUserStatJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(monthlyUserStatStep())
        .build();
  }

  @Bean
  public Job allTimeUserStatJob() {
    return new JobBuilder("allTimeUserStatJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(allTimeUserStatStep())
        .build();
  }
}