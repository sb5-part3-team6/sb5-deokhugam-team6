package com.codeit.project.deokhugam.batch.job;

import com.codeit.project.deokhugam.batch.processor.review.DailyStatProcessor;
import com.codeit.project.deokhugam.batch.processor.review.MonthlyStatProcessor;
import com.codeit.project.deokhugam.batch.processor.review.WeeklyStatProcessor;
import com.codeit.project.deokhugam.batch.reader.review.DailyStatReader;
import com.codeit.project.deokhugam.batch.reader.review.MonthlyStatReader;
import com.codeit.project.deokhugam.batch.reader.review.WeeklyStatReader;
import com.codeit.project.deokhugam.batch.writer.ReviewStatWriter;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.review.dto.ReviewStatDto;
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

    private final DailyStatReader dailyStatReader;
    private final WeeklyStatReader weeklyStatReader;
    private final MonthlyStatReader monthlyStatReader;

    private final DailyStatProcessor dailyStatProcessor;
    private final WeeklyStatProcessor weeklyStatProcessor;
    private final MonthlyStatProcessor monthlyStatProcessor;

    private final ReviewStatWriter reviewStatWriter;

    @Bean
    public Step dailyReviewStatStep() {
        return new StepBuilder("dailyReviewStatStep", jobRepository)
                .<ReviewStatDto, Rank>chunk(100, transactionManager)
                .reader(dailyStatReader)
                .processor(dailyStatProcessor)
                .writer(reviewStatWriter)
                .build();
    }

    @Bean
    public Step weeklyReviewStatStep() {
        return new StepBuilder("weeklyReviewStatStep", jobRepository)
                .<ReviewStatDto, Rank>chunk(100, transactionManager)
                .reader(weeklyStatReader)
                .processor(weeklyStatProcessor)
                .writer(reviewStatWriter)
                .build();
    }

    @Bean
    public Step monthlyReviewStatStep() {
        return new StepBuilder("monthlyReviewStatStep", jobRepository)
                .<ReviewStatDto, Rank>chunk(100, transactionManager)
                .reader(monthlyStatReader)
                .processor(monthlyStatProcessor)
                .writer(reviewStatWriter)
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
}
