package com.codeit.project.deokhugam.domain.review.mapper;

import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.review.dto.PopularReviewDto;
import com.codeit.project.deokhugam.domain.review.dto.ReviewDto;
import com.codeit.project.deokhugam.domain.review.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "bookId", source = "review.book.id")
    @Mapping(target = "bookTitle", source = "review.book.title")
    @Mapping(target = "bookThumbnailUrl", source = "review.book.thumbnailUrl")
    @Mapping(target = "userId", source = "review.user.id")
    @Mapping(target = "userNickname", source = "review.user.nickname")
    @Mapping(target = "likeCount", source = "likeCount")
    @Mapping(target = "commentCount", source = "commentCount")
    @Mapping(target = "likedByMe", source = "likedByMe")
    ReviewDto toDto(Review review, int likeCount, int commentCount, boolean likedByMe);

    @Mapping(target = "id", source = "rank.id")
    @Mapping(target = "reviewId", source = "review.id")
    @Mapping(target = "bookId", source = "review.book.id")
    @Mapping(target = "bookTitle", source = "review.book.title")
    @Mapping(target = "bookThumbnailUrl", source = "review.book.thumbnailUrl")
    @Mapping(target = "userId", source = "review.user.id")
    @Mapping(target = "userNickname", source = "review.user.nickname")
    @Mapping(target = "reviewContent", source = "review.content")
    @Mapping(target = "reviewRating", source = "review.rating")
    @Mapping(target = "period", source = "rank.type")
    @Mapping(target = "rank", source = "rank.rankNo")
    @Mapping(target = "score", source = "rank.score")
    @Mapping(target = "likeCount", source = "likeCount")
    @Mapping(target = "commentCount", source = "commentCount")
    @Mapping(target = "createdAt", source = "rank.createdAt")
    PopularReviewDto toPopularDto(Review review, Rank rank, int likeCount, int commentCount);
}
