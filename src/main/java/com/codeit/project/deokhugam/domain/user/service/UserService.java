package com.codeit.project.deokhugam.domain.user.service;

import com.codeit.project.deokhugam.domain.comment.repository.CommentRepository;
import com.codeit.project.deokhugam.domain.rank.dto.RankSearchCommand;
import com.codeit.project.deokhugam.domain.rank.entity.Rank;
import com.codeit.project.deokhugam.domain.rank.entity.RankTarget;
import com.codeit.project.deokhugam.domain.rank.entity.RankType;
import com.codeit.project.deokhugam.domain.rank.service.RankService;
import com.codeit.project.deokhugam.domain.review.repository.ReviewLikeRepository;
import com.codeit.project.deokhugam.domain.review.repository.ReviewRepository;
import com.codeit.project.deokhugam.domain.user.dto.PowerUserDto;
import com.codeit.project.deokhugam.domain.user.dto.UserDto;
import com.codeit.project.deokhugam.domain.user.dto.UserLoginRequest;
import com.codeit.project.deokhugam.domain.user.dto.UserRegisterRequest;
import com.codeit.project.deokhugam.domain.user.dto.UserUpdateRequest;
import com.codeit.project.deokhugam.domain.user.entity.User;
import com.codeit.project.deokhugam.domain.user.exception.DeleteNotAllowedException;
import com.codeit.project.deokhugam.domain.user.exception.EmailDuplicationException;
import com.codeit.project.deokhugam.domain.user.exception.LoginInputInvalidException;
import com.codeit.project.deokhugam.domain.user.exception.NicknameDuplicationException;
import com.codeit.project.deokhugam.domain.user.exception.UserAlreadyDeletedException;
import com.codeit.project.deokhugam.domain.user.exception.UserNotFoundException;
import com.codeit.project.deokhugam.domain.user.repository.UserRepository;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final RankService rankService;
  private final ReviewRepository reviewRepository;
  private final ReviewLikeRepository reviewLikeRepository;
  private final CommentRepository commentRepository;

  @Transactional
  public UserDto create(UserRegisterRequest request) {

    if(userRepository.existsByEmail(request.email())) {
      log.error("이미 존재하는 이메일, Email = {}", request.email());
      throw new EmailDuplicationException().withEmail(request.email());
    }

    if(userRepository.existsByNickname(request.nickname())) {
      log.error("이미 존재하는 닉네임, Nickname = {}", request.nickname());
      throw new NicknameDuplicationException().withNickname(request.nickname());
    }

    User user = new User(request.email(), request.nickname(), request.password());
    User saved = userRepository.save(user);

    return new UserDto(saved.getId().toString(), saved.getEmail(), saved.getNickname(), saved.getCreatedAt());
  }

  @Transactional
  public UserDto login(UserLoginRequest request) {
    User findUser = userRepository.findByEmail(request.email())
        .orElseThrow(() -> {
            log.warn("존재하지 않는 이메일, Email = {}", request.email());
            throw new LoginInputInvalidException().withEmail(request.email());
        });

    if(!findUser.getPassword().equals(request.password())) {
      log.warn("비밀번호 불일치");
      throw new LoginInputInvalidException().withPassword(request.password());
    }

    return new UserDto(findUser.getId().toString(), findUser.getEmail(), findUser.getNickname(), findUser.getCreatedAt());
  }

  @Transactional(readOnly = true)
  public UserDto findById(String id) {
    User findUser = userRepository.findById(Long.parseLong(id))
        .orElseThrow(() -> {
          log.warn("존재하지 않는 사용자");
          throw new UserNotFoundException().withId(id);
        });

    return new UserDto(findUser.getId().toString(), findUser.getEmail(), findUser.getNickname(), findUser.getCreatedAt());
  }

  @Transactional
  public UserDto update(String id, UserUpdateRequest request) {
    User findUser = userRepository.findById(Long.parseLong(id))
        .orElseThrow(() -> {
          log.warn("존재하지 않는 사용자");
          throw new UserNotFoundException().withId(id);
        });

    if(userRepository.existsByNickname(request.nickname())) {
      log.error("이미 존재하는 닉네임, Nickname = {}", request.nickname());
      throw new NicknameDuplicationException().withNickname(request.nickname());
    }

    findUser.updateNickname(request.nickname());
    return new UserDto(findUser.getId().toString(), findUser.getEmail(), findUser.getNickname(), findUser.getCreatedAt());
  }

  @Transactional
  public void softDelete(String id) {
    User findUser = userRepository.findById(Long.parseLong(id))
        .orElseThrow(() -> {
          log.warn("존재하지 않는 사용자");
          throw new UserNotFoundException().withId(id);
        });

    if(findUser.getDeletedAt() == null) {
      findUser.softDelete();
    }
    else {
      log.warn("이미 삭제된 사용자, Email = {}", findUser.getEmail());
      throw new UserAlreadyDeletedException().withEmail(findUser.getEmail());
    }
  }

  @Transactional
  public void hardDelete(String id) {
    User findUser = userRepository.findById(Long.parseLong(id))
        .orElseThrow(() -> {
          log.warn("존재하지 않는 사용자");
          throw new UserNotFoundException().withId(id);
        });

    if(findUser.getDeletedAt() == null) {
      log.warn("soft delete가 우선적으로 수행되어야함.");
      throw new DeleteNotAllowedException().withEmail(findUser.getEmail());
    }
    else {
      userRepository.delete(findUser);
    }
  }

  public PageResponse getRank(String direction, LocalDate cursor, LocalDate after, Integer limit) {
    List<Rank> ranks = rankService.findRank(RankSearchCommand.builder()
        .target(RankTarget.USER)
        .type(RankType.ALL_TIME)
        .direction(direction)
        .cusor(cursor.toString())
        .after(after.toString())
        .limit(Long.parseLong(limit.toString()))
        .build());

    List<PowerUserDto> content = new ArrayList<>();
    for(Rank rank : ranks) {
      User user = userRepository.findById(rank.getTargetId())
          .orElseThrow(() -> new UserNotFoundException().withId(rank.getTargetId().toString()));

      PowerUserDto powerUser = PowerUserDto.builder()
          .userId(user.getId().toString())
          .nickname(user.getNickname())
          .period(RankType.ALL_TIME)
          .createdAt(rank.getCreatedAt().toString())
          .rank(rank.getRankNo())
          .score(Integer.parseInt(rank.getScore().toString()))
          .reviewScoreSum(null)
          .likeCount(null)
          .commentCount(null)
          .build();

      content.add(powerUser);
    }

    return PageResponse.builder()
        .content(content)
        .nextCursor(null)
        .nextAfter(null)
        .size(10)
        .totalElements(10L)
        .hasNext(false)
        .build();
  }
}
