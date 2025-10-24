package com.codeit.project.deokhugam.domain.review.controller;

import com.codeit.project.deokhugam.domain.review.dto.ReviewDto;
import com.codeit.project.deokhugam.domain.review.dto.ReviewQueryParams;
import com.codeit.project.deokhugam.domain.review.service.ReviewService;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
public class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @MockitoBean
    private ReviewService reviewService;

    private ReviewDto reviewDto;
    private PageResponse pageResponse;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        reviewDto = ReviewDto.builder().id(1L)
                .bookId(1L).bookTitle("testBook1").bookThumbnailUrl("https://example.com/cleancode.jpg")
                .userId(2L).userNickname("testUser")
                .content("testContent").rating(1)
                .likeCount(10).commentCount(5).likedByMe(true)
                .createdAt(now).updatedAt(null)
                .build();

        pageResponse = PageResponse.builder()
                .content(List.of(reviewDto))
                .nextCursor("21").nextAfter("2025-10-16T07:06:52.049097Z")
                .size(20).totalElements(113L)
                .hasNext(true)
                .build();
    }

    @Test
    @DisplayName("GET /api/reviews - 정상 조회")
    void getReviewList_success() throws Exception {
        BDDMockito.given(reviewService.list(any(ReviewQueryParams.class), eq(2L)))
                .willReturn(pageResponse);

        mockMvc.perform(get("/api/reviews")
                        .header("Deokhugam-Request-User-ID", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].bookTitle").value("testBook1"))
                .andExpect(jsonPath("$.totalElements").value(113L));
    }
}
