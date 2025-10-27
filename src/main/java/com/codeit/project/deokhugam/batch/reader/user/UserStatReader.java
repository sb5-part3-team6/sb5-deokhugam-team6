package com.codeit.project.deokhugam.batch.reader.user;

import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import com.codeit.project.deokhugam.domain.user.dto.UserStatDto;
import com.codeit.project.deokhugam.domain.user.repository.UserRepository;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
@StepScope
@RequiredArgsConstructor
public class UserStatReader implements ItemReader<UserStatDto> {

  private UserRepository userRepository;
  private RankType rankType;
  private Iterator<UserStatDto> iterator;

  public UserStatReader(UserRepository userRepository, RankType rankType) {
    this.userRepository = userRepository;
    this.rankType = rankType;
  }

  @Override
  public UserStatDto read() {
    if (iterator == null) {
      switch (rankType) {
        case DAILY -> iterator = userRepository.getDailyStats()
                                               .iterator();
        case WEEKLY -> iterator = userRepository.getWeeklyStats()
                                                .iterator();
        case MONTHLY -> iterator = userRepository.getMonthlyStats()
                                                 .iterator();
        default -> throw new IllegalArgumentException("Unsupported RankType: " + rankType);
      }
    }
    return iterator.hasNext() ? iterator.next() : null;
  }
}