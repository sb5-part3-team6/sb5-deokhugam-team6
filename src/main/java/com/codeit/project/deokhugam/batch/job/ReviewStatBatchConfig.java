package com.codeit.project.deokhugam.batch.job;

import com.codeit.project.deokhugam.batch.processor.ReviewStatProcessor;
import com.codeit.project.deokhugam.batch.reader.ReviewStatReader;
import com.codeit.project.deokhugam.batch.writer.StatWriter;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.entity.RankTarget;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import com.codeit.project.deokhugam.domain.review.dto.ReviewStatDto;
import com.codeit.project.deokhugam.domain.review.repository.ReviewStatRepository;
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
public class ReviewStatBatchConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final ReviewStatRepository reviewStatRepository;
  private final StatWriter statWriter;

  @Bean
  public Step dailyReviewStatStep() {
    return new StepBuilder("dailyReviewStatStep", jobRepository)
        .<ReviewStatDto, Rank>chunk(100, transactionManager)
        .reader(new ReviewStatReader(reviewStatRepository, RankType.DAILY))
        .processor(new ReviewStatProcessor(RankType.DAILY, RankTarget.REVIEW))
        .writer(statWriter)
        .build();
  }

  @Bean
  public Step weeklyReviewStatStep() {
    return new StepBuilder("weeklyReviewStatStep", jobRepository)
        .<ReviewStatDto, Rank>chunk(100, transactionManager)
        .reader(new ReviewStatReader(reviewStatRepository, RankType.WEEKLY))
        .processor(new ReviewStatProcessor(RankType.WEEKLY, RankTarget.REVIEW))
        .writer(statWriter)
        .build();
  }

  @Bean
  public Step monthlyReviewStatStep() {
    return new StepBuilder("monthlyReviewStatStep", jobRepository)
        .<ReviewStatDto, Rank>chunk(100, transactionManager)
        .reader(new ReviewStatReader(reviewStatRepository, RankType.MONTHLY))
        .processor(new ReviewStatProcessor(RankType.MONTHLY, RankTarget.REVIEW))
        .writer(statWriter)
        .build();
  }

  @Bean
  public Step allTimeReviewStatStep() {
    return new StepBuilder("allTimeReviewStatStep", jobRepository)
        .<ReviewStatDto, Rank>chunk(100, transactionManager)
        .reader(new ReviewStatReader(reviewStatRepository, RankType.ALL_TIME))
        .processor(new ReviewStatProcessor(RankType.MONTHLY, RankTarget.REVIEW))
        .writer(statWriter)
        .build();
  }

  @Bean
  public Job dailyReviewStatJob() {
    return new JobBuilder("dailyReviewStatJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(dailyReviewStatStep())
        .build();
  }

  @Bean
  public Job weeklyReviewStatJob() {
    return new JobBuilder("weeklyReviewStatJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(weeklyReviewStatStep())
        .build();
  }

  @Bean
  public Job monthlyReviewStatJob() {
    return new JobBuilder("monthlyReviewStatJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(monthlyReviewStatStep())
        .build();
  }

  @Bean
  public Job allTimeReviewStatJob() {
    return new JobBuilder("allTimeReviewStatJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(allTimeReviewStatStep())
        .build();
  }
}
