package com.codeit.project.deokhugam.batch.reader.review;

import com.codeit.project.deokhugam.domain.review.dto.ReviewStatDto;
import com.codeit.project.deokhugam.domain.review.repository.ReviewStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
@StepScope
@RequiredArgsConstructor
public class WeeklyStatReader implements ItemReader<ReviewStatDto> {

    private final ReviewStatRepository reviewStatRepository;
    private Iterator<ReviewStatDto> item;

    @Override
    public ReviewStatDto read() {
        if (item == null) {
            item = reviewStatRepository.getWeeklyReviewStats().iterator();
        }
        return item.hasNext() ? item.next() : null;
    }
}
