package com.codeit.project.deokhugam.domain.rank.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.codeit.project.deokhugam.domain.book.entity.Book;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import com.codeit.project.deokhugam.domain.review.entity.Review;
import com.codeit.project.deokhugam.domain.user.entity.User;
import com.codeit.project.deokhugam.global.config.QuerydslConfig;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QuerydslConfig.class)
class RankRepositoryCustomTest {

  @Autowired
  private EntityManager em;

  @Autowired
  private RankRepository rankRepository;

  @Autowired
  private RankRepositoryCustomImpl rankRepositoryImpl;

  private Review review;

  @BeforeEach
  void setUp() {
    User user = new User("user@test.com", "user1", "pw1234");
    em.persist(user);

    Book book = new Book("Clean Code", "Robert C. Martin", "클린 코드 설명", "인사이트",
        LocalDate.of(2008, 8, 1), "9788966260959", "https://example.com/cleancode.jpg");
    em.persist(book);

    review = new Review(user, book, "좋은 책이에요", 5);
    em.persist(review);

    em.flush();
    em.clear();
  }

  @Test
  @DisplayName("QueryDSL - deleteByDateAndTypeDaily()는 오늘자 Daily 데이터만 삭제")
  void deleteByDateAndTypeDaily_onlyToday() {
    for (int i = 1; i <= 3; i++) {
      Rank rank = new Rank("REVIEW", review.getId(), RankType.DAILY.name(), i,
          BigDecimal.valueOf(100 - i));
      em.persist(rank);
    }
    Rank weeklyRank = new Rank("REVIEW", review.getId(), RankType.WEEKLY.name(), 1,
        BigDecimal.valueOf(999));
    em.persist(weeklyRank);

    em.flush();
    em.clear();

    List<Rank> allBefore = rankRepository.findAll();
    assertThat(allBefore).hasSize(4);

    rankRepositoryImpl.deleteByDateAndTypeDaily(LocalDate.now());
    em.flush();
    em.clear();

    List<Rank> allAfter = rankRepository.findAll();
    assertThat(allAfter).hasSize(1);
  }
}