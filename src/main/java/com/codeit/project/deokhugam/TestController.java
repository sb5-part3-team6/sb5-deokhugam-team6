package com.codeit.project.deokhugam;

import com.codeit.project.deokhugam.domain.book.dto.BookDto;
import com.codeit.project.deokhugam.openapi.api.NaverBookApiClient;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test")
public class TestController {

    private final JobLauncher jobLauncher;
    private final Job dailyReviewStatJob;
    private final NaverBookApiClient naverBookApiClient;

    @GetMapping("/force-fetch")
    public String forceFetchNews() {
        try {
            runJob(dailyReviewStatJob, "dailyReview");

            return "OK - Batch job has been requested.";

        } catch (Exception e) {
            // Job 실행 중 발생할 수 있는 예외를 처리합니다.
            return "Error - Failed to execute batch job: " + e.getMessage();
        }
    }

    private void runJob(Job job, String name) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("requestDate", String.valueOf(System.currentTimeMillis()))
                    .addString("jobName", name) // 중복 방지
                    .toJobParameters();

            jobLauncher.run(job, jobParameters);
            log.info("{} batch job 이 성공적으로 실행되었습니다.", name);

        } catch (Exception e) {
            log.error("{} batch job 실패", name, e);
        }
    }

    @GetMapping("/naver/book")
    public void getBook(@PathParam("isbn") String isbn) {
        naverBookApiClient.fetchBooks(isbn);
    }
}
