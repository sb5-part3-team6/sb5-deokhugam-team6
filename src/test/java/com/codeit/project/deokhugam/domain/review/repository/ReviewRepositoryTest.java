package com.codeit.project.deokhugam.domain.review.repository;

import com.codeit.project.deokhugam.domain.book.entity.Book;
import com.codeit.project.deokhugam.domain.book.repository.BookRepository;
import com.codeit.project.deokhugam.domain.review.controller.ReviewController;
import com.codeit.project.deokhugam.domain.review.dto.ReviewQueryParams;
import com.codeit.project.deokhugam.domain.review.entity.Review;
import com.codeit.project.deokhugam.domain.user.entity.User;
import com.codeit.project.deokhugam.domain.user.repository.UserRepository;
import com.codeit.project.deokhugam.global.config.QuerydslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Incubating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(ReviewRepositoryCustomImpl.class)
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository jpaReviewRepository;

    @Autowired
    private ReviewRepositoryCustomImpl reviewRepository;

    @Autowired
    private BookRepository jpaBookRepository;

    @Autowired
    private UserRepository jpaUserRepository;

    @Test
    @DisplayName("리뷰 목록 조회 - 기본 조건 createdAt desc 정렬")
    void list_success() {

        LocalDate now = LocalDate.now();

        User user = jpaUserRepository.save(new User("aaa@email.com", "testUser1", "1234"));
        Book book = jpaBookRepository.save(new Book("첫 번째 도서",
                "저자1",
                "도서에 대한 설명입니다.",
                "출판사",
                 now,
                "",
                ""));


        Review review1 = new Review(user, book, "첫 번째 리뷰", 3);
        jpaReviewRepository.save(review1);

        ReviewQueryParams params = new ReviewQueryParams(1L,
                1L,
                null,
                "desc",
                "createdAt",
                null,
                null,
                50,
                1L);

        List<Review> result = reviewRepository.list(params);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getContent()).isEqualTo("두 번째 리뷰");
    }




}
