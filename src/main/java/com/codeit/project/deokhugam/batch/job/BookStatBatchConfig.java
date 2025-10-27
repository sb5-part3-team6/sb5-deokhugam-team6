package com.codeit.project.deokhugam.batch.job;

import com.codeit.project.deokhugam.batch.processor.book.BookStatProcessor;
import com.codeit.project.deokhugam.batch.reader.book.BookStatReader;
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
  public Step monthlyBookStatStep() {
    return new StepBuilder("monthlyBookStatStep", jobRepository)
        .<BookStatDto, Rank>chunk(100, transactionManager)
        .reader(new BookStatReader(bookRepository, RankType.MONTHLY))
        .processor(new BookStatProcessor(RankType.MONTHLY, RankTarget.BOOK))
        .writer(statWriter)
        .build();
  }


  @Bean
  public Job monthlyBookStatJob() {
    return new JobBuilder("monthlyBookStatJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(monthlyBookStatStep())
        .build();
  }
}