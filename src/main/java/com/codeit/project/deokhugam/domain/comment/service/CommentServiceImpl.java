package com.codeit.project.deokhugam.domain.comment.service;

import com.codeit.project.deokhugam.domain.comment.dto.event.CommentUpdateEvent;
import com.codeit.project.deokhugam.domain.comment.dto.request.CommentCreateRequest;
import com.codeit.project.deokhugam.domain.comment.dto.response.CommentDto;
import com.codeit.project.deokhugam.domain.comment.dto.request.CommentUpdateRequest;
import com.codeit.project.deokhugam.domain.comment.entity.Comment;
import com.codeit.project.deokhugam.domain.comment.mapper.CommentMapper;
import com.codeit.project.deokhugam.domain.comment.repository.CommentRepository;
import com.codeit.project.deokhugam.domain.comment.dto.event.CommentDeleteEvent;
import com.codeit.project.deokhugam.domain.comment.dto.event.CommentEvent;
import com.codeit.project.deokhugam.domain.review.entity.Review;
import com.codeit.project.deokhugam.domain.review.repository.ReviewRepository;
import com.codeit.project.deokhugam.domain.user.entity.User;
import com.codeit.project.deokhugam.domain.user.repository.UserRepository;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final CommentMapper commentMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public CommentDto create(CommentCreateRequest req) {
        User userId = userRepository.findById(req.userId())
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다 : " + req.userId()));
        Review reviewId = reviewRepository.findById(req.reviewId())
                .orElseThrow(() -> new NoSuchElementException("리뷰를 찾을 수 없습니다 : " + req.reviewId()));

        Comment comment = new Comment(reviewId, userId, req.content());
        commentRepository.save(comment);

        eventPublisher.publishEvent(new CommentEvent(reviewId, userId, comment.getContent()));

        return commentMapper.toCommentDto(comment);
    }

    @Override
    @Transactional
    public CommentDto update(Long id, CommentUpdateRequest req, Long currentUserId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다 : " + id));
        if (comment.getDeletedAt() != null) {
            throw new IllegalStateException("삭제된 댓글은 수정할 수 없습니다.");
        }
        checkAuthor(comment, currentUserId);

        String oldData = comment.getContent();

        comment.updateContent(req.content());

        commentRepository.save(comment);

        eventPublisher.publishEvent(new CommentUpdateEvent(comment.getReview(), comment.getUser(),
            oldData, comment.getContent() ));

        return commentMapper.toCommentDto(comment);
    }

    @Override
    @Transactional
    public void deleteSoft(Long id, Long currentUserId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없습니다 : " + id));

        checkAuthor(comment, currentUserId);

        if (comment.getDeletedAt() != null) {
            throw new IllegalStateException("이미 삭제된 댓글입니다: " + id);
        }
        comment.softDelete();

        commentRepository.save(comment);

        eventPublisher.publishEvent(new CommentDeleteEvent(comment.getReview(), comment.getUser(), comment.getContent(), true));
    }
    @Override
    @Transactional
    public void delete(Long id, Long currentUserId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없습니다 : " + id));

        checkAuthor(comment, currentUserId);

        if (comment.getDeletedAt() != null) {
            throw new IllegalStateException("이미 삭제된 댓글입니다: " + id);
        }
        commentRepository.deleteById(id);

        eventPublisher.publishEvent(new CommentDeleteEvent(comment.getReview(), comment.getUser(), comment.getContent(), true));
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto findById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없습니다 : " + id));
        return commentMapper.toCommentDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse getByCursor(Long reviewId, LocalDateTime after, Long cursor, int limit, String direction) {
        return commentRepository.findCommentsByCursor(reviewId, after, cursor, limit, direction);
    }

    private void checkAuthor(Comment comment, Long currentUserId){
        if(!comment.getUser().getId().equals(currentUserId)){
            throw new RuntimeException("댓글 삭제/수정 권한이 없습니다.");
        }
    }

}