package com.codeit.project.deokhugam.batch.writer;

import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.repository.RankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@StepScope
@RequiredArgsConstructor
public class RankWriter implements ItemWriter<Rank> {

    private final RankRepository rankRepository;

    @Override
    public void write(Chunk<? extends Rank> chunk) {
        // TODO 1 필요하다면 하루에 한번만 실행 될 수 있도록 조회 체크 or 오늘자 삭제 후 재저장
        // TODO 2 알림 이벤트 발생
        AtomicInteger counter = new AtomicInteger(1);

        List<Rank> sorted = chunk.getItems().stream()
                .sorted(Comparator.comparing(Rank::getScore).reversed())
                .peek(rank -> rank.updateRankNo(counter.getAndIncrement()))
                .collect(Collectors.toList());

        rankRepository.saveAll(sorted);
    }
}