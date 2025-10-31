package com.codeit.project.deokhugam.domain.review.service;

import com.codeit.project.deokhugam.domain.book.entity.Book;
import com.codeit.project.deokhugam.domain.book.exception.detail.BookNotFoundException;
import com.codeit.project.deokhugam.domain.book.repository.BookRepository;
import com.codeit.project.deokhugam.domain.comment.dto.event.CommentDeleteEvent;
import com.codeit.project.deokhugam.domain.comment.repository.CommentRepository;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.repository.RankRepository;
import com.codeit.project.deokhugam.domain.review.dto.event.ReviewLikedDeleteEvent;
import com.codeit.project.deokhugam.domain.review.dto.request.ReviewCreateRequest;
import com.codeit.project.deokhugam.domain.review.dto.request.ReviewPopularQueryParams;
import com.codeit.project.deokhugam.domain.review.dto.request.ReviewQueryParams;
import com.codeit.project.deokhugam.domain.review.dto.request.ReviewUpdateRequest;
import com.codeit.project.deokhugam.domain.review.dto.response.PopularReviewDto;
import com.codeit.project.deokhugam.domain.review.dto.response.ReviewDto;
import com.codeit.project.deokhugam.domain.review.dto.response.ReviewLikeDto;
import com.codeit.project.deokhugam.domain.review.entity.Review;
import com.codeit.project.deokhugam.domain.review.entity.ReviewLike;
import com.codeit.project.deokhugam.domain.review.dto.event.ReviewLikedEvent;
import com.codeit.project.deokhugam.domain.review.exception.detail.ReviewAlreadyExistsException;
import com.codeit.project.deokhugam.domain.review.exception.detail.ReviewNotFoundException;
import com.codeit.project.deokhugam.domain.review.mapper.ReviewMapper;
import com.codeit.project.deokhugam.domain.review.repository.ReviewLikeRepository;
import com.codeit.project.deokhugam.domain.review.repository.ReviewRepository;
import com.codeit.project.deokhugam.domain.user.entity.User;
import com.codeit.project.deokhugam.domain.user.repository.UserRepository;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final RankRepository rankRepository;
    private final ReviewMapper reviewMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public ReviewDto create(ReviewCreateRequest request) {

        User user = verifyUserExists(request.userId());
        Book book = verifyBookExists(request.bookId());
        Review review = reviewRepository.findByUserIdAndBookId(user.getId(), book.getId());

        if(review == null) {
            Review newReview = reviewRepository.save(new Review(user, book, request.content(), request.rating()));
            return reviewMapper.toDto(newReview,0,0,false);
        } else {
            if(review.getDeletedAt() != null){
                hardDelete(review.getId(), user.getId());
                Review newReview = reviewRepository.save(new Review(user, book, request.content(), request.rating()));
                return reviewMapper.toDto(newReview,0,0,false);
            }
            throw new ReviewAlreadyExistsException();
        }
    }

    @Override
    @Transactional
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
        List<Review> reviewList = reviewRepository.list(params);
        User user = verifyUserExists(userId);

        Long total = reviewRepository.countTotal(params.bookId());
        boolean hasNext = reviewList.size() > params.limit();
        String nextCursor = null;
        String nextAfter = null;

        if(hasNext) {
            Review lastItem = reviewList.get(reviewList.size() - 1);
            reviewList.remove(lastItem);
            nextCursor = reviewList.get(reviewList.size()-1).getId().toString();
            nextAfter = reviewList.get(reviewList.size()-1).getCreatedAt().toString();
        }

        List<ReviewDto> content = reviewList.stream().map(review -> {
            int likeCount = reviewLikeRepository.countAllByReviewId(review.getId());
            int commentCount =  commentRepository.countByReviewIdAndDeletedAtIsNull(review.getId());
            boolean likedByMe = reviewLikeRepository.existsByReviewIdAndUserId(review.getId(), user.getId());
            return reviewMapper.toDto(review,likeCount,commentCount,likedByMe);
        }).toList();

        return PageResponse.builder()
                .content(content)
                .nextCursor(nextCursor)
                .size(reviewList.size())
                .totalElements(total)
                .hasNext(hasNext)
                .nextAfter(nextAfter)
                .build();
    }

    public ReviewDto detail(Long reviewId, Long userId) {
        User user = verifyUserExists(userId);
        Review review = verifyReviewExists(reviewId);

        boolean likedByMe = reviewLikeRepository.existsByReviewIdAndUserId(review.getId(), user.getId());
        int likeCount = reviewLikeRepository.countAllByReviewId(review.getId());
        int commentCount =  commentRepository.countByReviewIdAndDeletedAtIsNull(review.getId());

        return reviewMapper.toDto(review,likeCount,commentCount,likedByMe);
    }

    public PageResponse popularList(ReviewPopularQueryParams params) {

        List<Rank> ranksList = reviewRepository.findRanksByType(params.period(), params.direction(), params.limit());

        Long total = rankRepository.countAllByTypeForReview(params.period());
        boolean hasNext = ranksList.size() > params.limit();
        String nextCursor = null;
        String nextAfter = null;

        if(hasNext) {
            Rank lastItem = ranksList.get(ranksList.size() - 1);
            nextCursor = lastItem.getId().toString();
            nextAfter = lastItem.getCreatedAt().toString();
            ranksList.remove(lastItem);
        }

        List<PopularReviewDto> content = ranksList.stream().map(rank -> {
            Review review = verifyReviewExists(rank.getTargetId());
            int likeCount = reviewLikeRepository.countAllByReviewId(review.getId());
            int commentCount =  commentRepository.countByReviewIdAndDeletedAtIsNull(review.getId());
            return reviewMapper.toPopularDto(review, rank,likeCount,commentCount);
        }).toList();

        return PageResponse.builder()
                .content(content)
                .nextCursor(nextCursor)
                .size(ranksList.size())
                .totalElements(total)
                .hasNext(hasNext)
                .nextAfter(nextAfter)
                .build();
    }

    @Override
    public ReviewDto update(Long reviewId, Long userId, ReviewUpdateRequest request) {
        Review review = verifyReviewExists(reviewId);
        User user = verifyUserExists(userId);

        if(!review.getUser().getId().equals(user.getId())) throw new IllegalArgumentException("리뷰를 삭제할 권한이 없습니다.");

        review.update(request.content(), request.rating());
        reviewRepository.save(review);

        int likeCount = reviewLikeRepository.countAllByReviewId(review.getId());
        int commentCount =  commentRepository.countByReviewIdAndDeletedAtIsNull(review.getId());
        boolean likedByMe = reviewLikeRepository.existsByReviewIdAndUserId(review.getId(), user.getId());

        return reviewMapper.toDto(review,likeCount,commentCount,likedByMe);
    }

    @Override
    public void softDelete(Long reviewId, Long userId) {
        Review review = verifyReviewExists(reviewId);

        if (!review.getUser().getId().equals(userId)) throw new IllegalArgumentException("리뷰를 삭제할 권한이 없습니다.");

        review.softDelete();
        reviewRepository.save(review);

        eventPublisher.publishEvent(new CommentDeleteEvent(review, null, null, false));
    }

    @Override
    public void hardDelete(Long reviewId, Long userId) {
        Review review = verifyReviewExists(reviewId);

        if (!review.getUser().getId().equals(userId)) throw new IllegalArgumentException("리뷰를 삭제할 권한이 없습니다.");

        reviewLikeRepository.deleteAllByReviewId(reviewId);
        commentRepository.deleteAllByReviewId(reviewId);
        reviewRepository.delete(review);

        eventPublisher.publishEvent(new CommentDeleteEvent(review, null, null, false));
    }

    private User verifyUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
    }

    private Book verifyBookExists(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
    }

    private Review verifyReviewExists(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
    }

    private boolean toggleReviewLike(Review review, User user) {
        Optional<ReviewLike> maybeLike = reviewLikeRepository.findByReviewIdAndUserId(review.getId(), user.getId());

        if (maybeLike.isPresent()) {
            reviewLikeRepository.delete(maybeLike.get());
            eventPublisher.publishEvent(new ReviewLikedDeleteEvent(review, user, true));
            return false;
        }

        ReviewLikedEvent event = new ReviewLikedEvent(review, user);
        eventPublisher.publishEvent(event);

        reviewLikeRepository.save(new ReviewLike(review, user));
        return true;
    }
}