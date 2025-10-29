package com.codeit.project.deokhugam.domain.user.service;

import com.codeit.project.deokhugam.domain.comment.repository.CommentRepository;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.repository.RankRepository;
import com.codeit.project.deokhugam.domain.review.entity.Review;
import com.codeit.project.deokhugam.domain.review.exception.detail.ReviewNotFoundException;
import com.codeit.project.deokhugam.domain.review.repository.ReviewLikeRepository;
import com.codeit.project.deokhugam.domain.review.repository.ReviewRepository;
import com.codeit.project.deokhugam.domain.user.dto.request.PowerUserQueryParams;
import com.codeit.project.deokhugam.domain.user.dto.request.UserLoginRequest;
import com.codeit.project.deokhugam.domain.user.dto.request.UserRegisterRequest;
import com.codeit.project.deokhugam.domain.user.dto.request.UserUpdateRequest;
import com.codeit.project.deokhugam.domain.user.dto.response.PowerUserDto;
import com.codeit.project.deokhugam.domain.user.dto.response.UserDto;
import com.codeit.project.deokhugam.domain.user.entity.User;
import com.codeit.project.deokhugam.domain.user.exception.detail.DeleteNotAllowedException;
import com.codeit.project.deokhugam.domain.user.exception.detail.EmailDuplicationException;
import com.codeit.project.deokhugam.domain.user.exception.detail.LoginInputInvalidException;
import com.codeit.project.deokhugam.domain.user.exception.detail.NicknameDuplicationException;
import com.codeit.project.deokhugam.domain.user.exception.detail.UserAlreadyDeletedException;
import com.codeit.project.deokhugam.domain.user.exception.detail.UserNotFoundException;
import com.codeit.project.deokhugam.domain.user.repository.UserRepository;
import com.codeit.project.deokhugam.domain.user.util.PasswordUtil;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RankRepository rankRepository;
  private final ReviewRepository reviewRepository;
  private final ReviewLikeRepository reviewLikeRepository;
  private final CommentRepository commentRepository;

  @Override
  @Transactional
  public UserDto create(UserRegisterRequest request) {

    if (userRepository.existsByEmail(request.email())) {
      log.error("이미 존재하는 이메일, Email = {}", request.email());
      throw new EmailDuplicationException().withEmail(request.email());
    }

    if (userRepository.existsByNickname(request.nickname())) {
      log.error("이미 존재하는 닉네임, Nickname = {}", request.nickname());
      throw new NicknameDuplicationException().withNickname(request.nickname());
    }

    String encodedPassword = PasswordUtil.encrypt(request.password());
    User user = new User(request.email(), request.nickname(), encodedPassword);
    User saved = userRepository.save(user);

    return new UserDto(saved.getId()
                            .toString(), saved.getEmail(), saved.getNickname(),
        saved.getCreatedAt());
  }

  @Override
  @Transactional
  public UserDto login(UserLoginRequest request) {
    User findUser = userRepository.findByEmail(request.email())
                                  .orElseThrow(() -> {
                                    log.warn("존재하지 않는 이메일, Email = {}", request.email());
                                    throw new LoginInputInvalidException().withEmail(
                                        request.email());
                                  });

    if (!PasswordUtil.matches(request.password(), findUser.getPassword())) {
      log.warn("비밀번호 불일치");
      throw new LoginInputInvalidException().withPassword(request.password());
    }

    return new UserDto(findUser.getId()
                               .toString(), findUser.getEmail(), findUser.getNickname(),
        findUser.getCreatedAt());
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto findById(String id) {
    User findUser = userRepository.findById(Long.parseLong(id))
                                  .orElseThrow(() -> {
                                    log.warn("존재하지 않는 사용자");
                                    throw new UserNotFoundException().withId(id);
                                  });

    return new UserDto(findUser.getId()
                               .toString(), findUser.getEmail(), findUser.getNickname(),
        findUser.getCreatedAt());
  }

  @Override
  @Transactional
  public UserDto update(String id, UserUpdateRequest request) {
    User findUser = userRepository.findById(Long.parseLong(id))
                                  .orElseThrow(() -> {
                                    log.warn("존재하지 않는 사용자");
                                    throw new UserNotFoundException().withId(id);
                                  });

    if (userRepository.existsByNickname(request.nickname())) {
      log.error("이미 존재하는 닉네임, Nickname = {}", request.nickname());
      throw new NicknameDuplicationException().withNickname(request.nickname());
    }

    findUser.updateNickname(request.nickname());
    return new UserDto(findUser.getId()
                               .toString(), findUser.getEmail(), findUser.getNickname(),
        findUser.getCreatedAt());
  }

  @Override
  @Transactional
  public void softDelete(String id) {
    User findUser = userRepository.findById(Long.parseLong(id))
                                  .orElseThrow(() -> {
                                    log.warn("존재하지 않는 사용자");
                                    throw new UserNotFoundException().withId(id);
                                  });

    if (findUser.getDeletedAt() == null) {
      findUser.softDelete();
    } else {
      log.warn("이미 삭제된 사용자, Email = {}", findUser.getEmail());
      throw new UserAlreadyDeletedException().withEmail(findUser.getEmail());
    }
  }

  @Override
  @Transactional
  public void hardDelete(String id) {
    User findUser = userRepository.findById(Long.parseLong(id))
                                  .orElseThrow(() -> {
                                    log.warn("존재하지 않는 사용자");
                                    throw new UserNotFoundException().withId(id);
                                  });

    if (findUser.getDeletedAt() == null) {
      log.warn("soft delete가 우선적으로 수행되어야함.");
      throw new DeleteNotAllowedException().withEmail(findUser.getEmail());
    } else {
      userRepository.delete(findUser);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse powerList(PowerUserQueryParams params) {

    List<Rank> rankList = userRepository.findRankByType(params.period(), params.direction(),
        params.limit());
    Long total = rankRepository.countAllByTypeForUser(params.period());
    boolean hasNext = rankList.size() > params.limit();
    String nextCursor = null;
    String nextAfter = null;

    if (hasNext) {
      Rank lastItem = rankList.get(rankList.size() - 1);
      nextCursor = lastItem.getId()
                           .toString();
      nextAfter = lastItem.getCreatedAt()
                          .toString();
      rankList.remove(lastItem);
    }

    List<PowerUserDto> content = rankList.stream()
                                         .map(rank -> {
                                           User user = verifyUserExists(rank.getTargetId());
                                           double reviewScoreSum = getReviewScoreByUserId(
                                               user.getId(), params.period());
                                           int likeCount = reviewLikeRepository.countAllByUserId(
                                               user.getId());
                                           int commentCount = commentRepository.countAllByUserIdAndDeletedAtIsNull(
                                               user.getId());
                                           return PowerUserDto.builder()
                                                              .userId(user.getId()
                                                                          .toString())
                                                              .nickname(user.getNickname())
                                                              .period(rank.getType())
                                                              .createdAt(rank.getCreatedAt()
                                                                             .toString())
                                                              .rank(rank.getRankNo())
                                                              .score(rank.getScore())
                                                              .reviewScoreSum(reviewScoreSum)
                                                              .likeCount(likeCount)
                                                              .commentCount(commentCount)
                                                              .build();
                                         })
                                         .toList();

    return PageResponse.builder()
                       .content(content)
                       .nextCursor(nextCursor)
                       .size(rankList.size())
                       .totalElements(total)
                       .hasNext(hasNext)
                       .nextAfter(nextAfter)
                       .build();
  }

  private User verifyUserExists(Long userId) {
    return userRepository.findById(userId)
                         .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
  }

  private Review verifyReviewExists(Long reviewId) {
    return reviewRepository.findById(reviewId)
                           .orElseThrow(ReviewNotFoundException::new);
  }

  private double getReviewScoreByUserId(Long userId, String type) {

    List<Long> reviewIds = rankRepository.findReviewIds(type);
    List<Review> reviewList = reviewIds.stream()
                                       .map(this::verifyReviewExists)
                                       .toList();

    long count = reviewList.stream()
                           .filter(review -> review.getUser() != null && review.getUser()
                                                                               .getId()
                                                                               .equals(userId))
                           .count();

    return count * 0.5;
  }

}
