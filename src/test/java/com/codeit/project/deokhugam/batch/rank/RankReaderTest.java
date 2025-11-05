package com.codeit.project.deokhugam.batch.rank;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import com.codeit.project.deokhugam.batch.reader.RankReader;
import com.codeit.project.deokhugam.domain.book.dto.BookStatDto;
import com.codeit.project.deokhugam.domain.book.repository.BookRepository;
import com.codeit.project.deokhugam.domain.rank.entity.RankTarget;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import com.codeit.project.deokhugam.domain.review.dto.ReviewStatDto;
import com.codeit.project.deokhugam.domain.review.repository.ReviewRepository;
import com.codeit.project.deokhugam.domain.user.dto.UserStatDto;
import com.codeit.project.deokhugam.domain.user.repository.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RankReaderTest {

  @Mock
  private BookRepository bookRepository;
  @Mock
  private ReviewRepository reviewRepository;
  @Mock
  private UserRepository userRepository;

  @Test
  @DisplayName("BOOK 랭크 - DAILY 타입 데이터 정상 조회")
  void read_BOOK_DAILY() {
    List<BookStatDto> fakeBooks = List.of(
        new BookStatDto(1L, 5L, 4.5),
        new BookStatDto(2L, 10L, 3.8)
    );
    when(bookRepository.getStatsByPeriod(RankType.DAILY)).thenReturn(fakeBooks);

    RankReader reader = new RankReader(
        bookRepository,
        reviewRepository,
        userRepository,
        RankType.DAILY.name(),
        RankTarget.BOOK.name()
    );

    Object first = reader.read();
    Object second = reader.read();
    Object third = reader.read();

    assertThat(first).isEqualTo(fakeBooks.get(0));
    assertThat(second).isEqualTo(fakeBooks.get(1));
    assertThat(third).isNull();
  }

  @Test
  @DisplayName("REVIEW 랭크 - WEEKLY 타입 데이터 정상 조회")
  void read_REVIEW_WEEKLY() {
    List<ReviewStatDto> fakeReviews = List.of(
        new ReviewStatDto(1L, 10L, 2L),
        new ReviewStatDto(2L, 5L, 5L)
    );
    when(reviewRepository.getStatsByPeriod(RankType.WEEKLY)).thenReturn(fakeReviews);

    RankReader reader = new RankReader(
        bookRepository,
        reviewRepository,
        userRepository,
        RankType.WEEKLY.name(),
        RankTarget.REVIEW.name()
    );

    Object first = reader.read();
    Object second = reader.read();
    Object third = reader.read();

    assertThat(first).isEqualTo(fakeReviews.get(0));
    assertThat(second).isEqualTo(fakeReviews.get(1));
    assertThat(third).isNull();
  }

  @Test
  @DisplayName("USER 랭크 - MONTHLY 타입 데이터 정상 조회")
  void read_USER_MONTHLY() {
    List<UserStatDto> fakeUsers = List.of(
        new UserStatDto(1L, 3L, 1L, BigDecimal.TEN)
    );
    when(userRepository.getStatsByPeriod(RankType.MONTHLY)).thenReturn(fakeUsers);

    RankReader reader = new RankReader(
        bookRepository,
        reviewRepository,
        userRepository,
        RankType.MONTHLY.name(),
        RankTarget.USER.name()
    );

    Object first = reader.read();
    Object second = reader.read();

    assertThat(first).isEqualTo(fakeUsers.get(0));
    assertThat(second).isNull();
  }
}