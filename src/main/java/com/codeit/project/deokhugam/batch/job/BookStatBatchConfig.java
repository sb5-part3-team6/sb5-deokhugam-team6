package com.codeit.project.deokhugam.batch.job;

import com.codeit.project.deokhugam.batch.processor.BookStatProcessor;
import com.codeit.project.deokhugam.batch.reader.BookStatReader;
import com.codeit.project.deokhugam.batch.writer.StatWriter;
import com.codeit.project.deokhugam.domain.book.dto.BookStatDto;
import com.codeit.project.deokhugam.domain.book.repository.BookRepository;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.entity.RankTarget;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
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
public class BookStatBatchConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final BookRepository bookRepository;
  private final StatWriter statWriter;

  @Bean
  public Step dailyBookStatStep() {
    return new StepBuilder("dailyBookStatStep", jobRepository)
        .<BookStatDto, Rank>chunk(100, transactionManager)
        .reader(new BookStatReader(bookRepository, RankType.DAILY))
        .processor(new BookStatProcessor(RankType.DAILY, RankTarget.BOOK))
        .writer(statWriter)
        .build();
  }

  @Bean
  public Step weeklyBookStatStep() {
    return new StepBuilder("weeklyBookStatStep", jobRepository)
        .<BookStatDto, Rank>chunk(100, transactionManager)
        .reader(new BookStatReader(bookRepository, RankType.WEEKLY))
        .processor(new BookStatProcessor(RankType.WEEKLY, RankTarget.BOOK))
        .writer(statWriter)
        .build();
  }

  @Bean
  public Step monthlyBookStatStep() {
    return new StepBuilder("monthlyBookStatStep", jobRepository)
        .<BookStatDto, Rank>chunk(100, transactionManager)
        .reader(new BookStatReader(bookRepository, RankType.MONTHLY))
        .processor(new BookStatProcessor(RankType.MONTHLY, RankTarget.BOOK))
        .writer(statWriter)
        .build();
  }

  @Bean
  public Step allTimeBookStatStep() {
    return new StepBuilder("allTimeBookStatStep", jobRepository)
        .<BookStatDto, Rank>chunk(100, transactionManager)
        .reader(new BookStatReader(bookRepository, RankType.ALL_TIME))
        .processor(new BookStatProcessor(RankType.ALL_TIME, RankTarget.BOOK))
        .writer(statWriter)
        .build();
  }

  @Bean
  public Job dailyBookStatJob() {
    return new JobBuilder("dailyBookStatJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(dailyBookStatStep())
        .build();
  }

  @Bean
  public Job weeklyBookStatJob() {
    return new JobBuilder("weeklyBookStatJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(weeklyBookStatStep())
        .build();
  }

  @Bean
  public Job monthlyBookStatJob() {
    return new JobBuilder("monthlyBookStatJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(monthlyBookStatStep())
        .build();
  }

  @Bean
  public Job allTimeBookStatJob() {
    return new JobBuilder("allTimeBookStatJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(allTimeBookStatStep())
        .build();
  }
}