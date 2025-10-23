package com.codeit.project.deokhugam.domain.user.service;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;

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
}
