package com.codeit.project.deokhugam.batch.processor.review;

import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.review.dto.ReviewStatDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@StepScope
public class MonthlyStatProcessor implements ItemProcessor<ReviewStatDto, Rank> {

    @Override
    public Rank process(ReviewStatDto dto) {
        double score = (dto.likeCount() * 0.3) + (dto.commentCount() * 0.7);

        return new Rank(
                "REVIEW",
                dto.reviewId(),
                "MONTHLY",
                null,
                BigDecimal.valueOf(score)
        );
    }
}
