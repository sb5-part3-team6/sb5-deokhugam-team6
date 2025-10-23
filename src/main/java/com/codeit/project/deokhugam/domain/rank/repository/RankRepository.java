package com.codeit.project.deokhugam.domain.rank.repository;

import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RankRepository extends JpaRepository<Rank, Long> {
    @Query("SELECT COUNT(r) FROM Rank r WHERE r.target = 'REVIEW' AND r.type = :type")
    long countAllByTypeForReview(@Param("type") String type);
}
