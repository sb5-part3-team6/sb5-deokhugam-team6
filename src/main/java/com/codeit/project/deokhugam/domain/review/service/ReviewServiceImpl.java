package com.codeit.project.deokhugam.domain.review.service;

import com.codeit.project.deokhugam.domain.book.entity.Book;
import com.codeit.project.deokhugam.domain.book.repository.BookRepository;
import com.codeit.project.deokhugam.domain.comment.repository.CommentRepository;
import com.codeit.project.deokhugam.domain.review.dto.*;
import com.codeit.project.deokhugam.domain.review.entity.Review;
import com.codeit.project.deokhugam.domain.review.entity.ReviewLike;
import com.codeit.project.deokhugam.domain.review.repository.ReviewLikeRepository;
import com.codeit.project.deokhugam.domain.review.repository.ReviewRepository;
import com.codeit.project.deokhugam.domain.review.repository.ReviewRepositoryCustom;
import com.codeit.project.deokhugam.domain.user.entity.User;
import com.codeit.project.deokhugam.domain.user.repository.UserRepository;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewRepositoryCustom reviewRepositoryCustom;
    private final ReviewLikeRepository reviewLikeRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public ReviewDto create(ReviewCreateRequest request) {

        User user = verifyUserExists(request.userId());
        Book book = verifyBookExists(request.bookId());

        if (reviewRepository.existsByUserIdAndBookId(user.getId(), book.getId()))
            throw new IllegalArgumentException("이미 해당 책에 대한 리뷰가 존재합니다.");

        Review review = reviewRepository.save(new Review(user, book, request.content(), request.rating()));

        return ReviewDto.builder()
                .id(review.getId())
                .bookId(book.getId())
                .bookTitle(book.getTitle())
                .bookThumbnailUrl(book.getThumbnailUrl())
                .userId(user.getId())
                .userNickname(user.getNickname())
                .content(review.getContent())
                .rating(review.getRating())
                .likeCount(0)
                .commentCount(0)
                .likedByMe(false)
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    @Override
    public ReviewLikeDto createLike(Long reviewId, Long userId) {
        Review review = verifyReviewExists(reviewId);
        User user = verifyUserExists(userId);
        boolean reviewLike = toggleReviewLike(review, user);

        return ReviewLikeDto.builder()
                .reviewId(review.getId())
                .userId(user.getId())
                .liked(reviewLike)
                .build();
    }

    public PageResponse list(ReviewQueryParams params, Long userId) {
        List<Review> reviewList = reviewRepositoryCustom.list(params);
        User user = verifyUserExists(userId);

        Long total = reviewRepository.countTotal();
        boolean hasNext = reviewList.size() > params.limit();
        String nextCursor = null;
        String nextAfter = null;

        if(hasNext) {
            Review lastItem = reviewList.get(reviewList.size() - 1);
            nextCursor = lastItem.getId().toString();
            nextAfter = lastItem.getCreatedAt().toString();
            reviewList.remove(lastItem);
        }

        List<ReviewDto> content = reviewList.stream().map(review -> {
            Book book = review.getBook();
            int likeCount = reviewLikeRepository.countByReviewId(review.getId());
            int commentCount =  commentRepository.countByReviewId(review.getId());
            return ReviewDto.builder()
                    .id(review.getId())
                    .bookId(book.getId())
                    .bookTitle(book.getTitle())
                    .bookThumbnailUrl(book.getThumbnailUrl())
                    .userId(user.getId())
                    .userNickname(user.getNickname())
                    .content(review.getContent())
                    .rating(review.getRating())
                    .likeCount(likeCount)
                    .commentCount(commentCount)
                    .likedByMe(false)
                    .createdAt(review.getCreatedAt())
                    .updatedAt(review.getUpdatedAt())
                    .build();
        }).toList();

        return PageResponse.builder()
                .content(content)
                .nextCursor(nextCursor)
                .size(reviewList.size() - 1)
                .totalElements(total)
                .hasNext(hasNext)
                .nextAfter(nextAfter)
                .build();
    }

    public void detail(Long reviewId, Long userId) {

    }

    public void popularList(ReviewPopularQueryParams params) {

    }

    @Override
    public ReviewDto update(Long reviewId, Long userId, ReviewUpdateRequest request) {
        Review review = verifyReviewExists(reviewId);
        User user = verifyUserExists(userId);

        if(!review.getUser().getId().equals(user.getId())) throw new IllegalArgumentException("리뷰를 삭제할 권한이 없습니다.");

        review.update(request.content(), request.rating());
        Book book = review.getBook();

        Optional<ReviewLike> reviewLike = reviewLikeRepository.findByReviewIdAndUserId(review.getId(), user.getId());
        int likeCount = reviewLikeRepository.countByReviewId(review.getId());
        int commentCount =  commentRepository.countByReviewId(review.getId());

        return ReviewDto.builder()
                .id(review.getId())
                .bookId(book.getId())
                .bookTitle(book.getTitle())
                .bookThumbnailUrl(book.getThumbnailUrl())
                .userId(user.getId())
                .userNickname(user.getNickname())
                .content(review.getContent())
                .rating(review.getRating())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .likedByMe(reviewLike.isPresent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    @Override
    public void softDelete(Long reviewId, Long userId) {
        Review review = verifyReviewExists(reviewId);

        if (!review.getUser().getId().equals(userId)) throw new IllegalArgumentException("리뷰를 삭제할 권한이 없습니다.");

        review.softDelete();
        reviewRepository.save(review);
    }

    @Override
    public void hardDelete(Long reviewId, Long userId) {
        Review review = verifyReviewExists(reviewId);

        if (!review.getUser().getId().equals(userId)) throw new IllegalArgumentException("리뷰를 삭제할 권한이 없습니다.");

        reviewLikeRepository.deleteAllByReviewId(reviewId);
        commentRepository.deleteAllByReviewId(reviewId);
        reviewRepository.delete(review);
    }

    private User verifyUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
    }

    private Book verifyBookExists(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 도서 입니다."));
    }

    private Review verifyReviewExists(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));
    }

    private boolean toggleReviewLike(Review review, User user) {
        Optional<ReviewLike> maybeLike = reviewLikeRepository.findByReviewIdAndUserId(review.getId(), user.getId());

        if (maybeLike.isPresent()) {
            reviewLikeRepository.delete(maybeLike.get());
            return false;
        }

        reviewLikeRepository.save(new ReviewLike(review, user));
        return true;
    }
}
