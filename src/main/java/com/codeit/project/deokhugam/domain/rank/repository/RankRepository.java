package com.codeit.project.deokhugam.domain.rank.repository;

import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RankRepository extends JpaRepository<Rank, Long> {
    @Query("SELECT COUNT(r) FROM Rank r WHERE r.target = 'REVIEW' AND r.type = :type")
    Long countAllByTypeForReview(@Param("type") String type);

    @Query("SELECT COUNT(DISTINCT r.targetId) FROM Rank r WHERE r.target = 'USER' AND r.type = :type")
    Long countAllByTypeForUser(@Param("type") String type);

    @Query("SELECT COUNT(r) FROM Rank r WHERE r.target = 'BOOK' AND r.type = :type")
    Long countAllByTypeForBook(@Param("type") String type);

    @Query("SELECT r.targetId FROM Rank r WHERE r.target = 'REVIEW' AND r.type = :type GROUP BY r.targetId")
    List<Long> findReviewIds(@Param("type") String type);
}
