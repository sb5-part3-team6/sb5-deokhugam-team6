package com.codeit.project.deokhugam.domain.review.repository;

import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import com.codeit.project.deokhugam.domain.review.dto.request.ReviewQueryParams;
import com.codeit.project.deokhugam.domain.review.dto.ReviewStatDto;
import com.codeit.project.deokhugam.domain.review.entity.Review;
import java.util.List;

public interface ReviewRepositoryCustom {

  List<Review> list(ReviewQueryParams params);

  List<Rank> findRanksByType(String type, String direction, int limit);

  List<ReviewStatDto> getStatsByPeriod(RankType type);
}
