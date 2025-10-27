package com.codeit.project.deokhugam.domain.user.repository;

import com.codeit.project.deokhugam.domain.user.dto.UserStatDto;
import java.util.List;

public interface UserRepositoryCustom {

  List<UserStatDto> getDailyStats();

  List<UserStatDto> getWeeklyStats();

  List<UserStatDto> getMonthlyStats();
}