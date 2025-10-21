package com.codeit.project.deokhugam.domain.user.service;

import com.codeit.project.deokhugam.domain.user.dto.UserDto;
import com.codeit.project.deokhugam.domain.user.dto.UserRegisterRequest;
import com.codeit.project.deokhugam.domain.user.entity.User;
import com.codeit.project.deokhugam.domain.user.repository.UserRepository;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;

  private final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
  private final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";


  @Transactional
  public UserDto create(UserRegisterRequest request) {

    if(!isValidEmail(request.email())) {
      log.error("유효하지 않은 이메일 형식, Email = {}", request.email());
      throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
    }

    if(userRepository.existsByEmail(request.email())) {
      log.error("이미 존재하는 이메일, Email = {}", request.email());
      throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
    }

    if(!isValidNickname(request.nickname())) {
      log.error("이미 존재하는 닉네임이거나 너무 짧음, Nickname = {}", request.nickname());
      throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
    }

    if(!isValidPassword(request.password())) {
      log.error("잘못된 비밀번호 형식, Password = {}", request.password());
      throw new IllegalArgumentException("잘못된 비밀번호 형식입니다.");
    }

    User user = new User(request.email(), request.nickname(), request.password());
    User saved = userRepository.save(user);

    return new UserDto(saved.getId().toString(), saved.getEmail(), saved.getNickname(), saved.getCreatedAt());
  }

  private boolean isValidEmail(String email) {
    if (email == null || email.isEmpty() || email.isBlank()) {
      return false;
    }

    return Pattern.matches(EMAIL_PATTERN, email);
  }

  private boolean isValidNickname(String nickname) {
    if(userRepository.existsByNickname(nickname) || nickname.length() < 2) {
      return false;
    }

    return true;
  }

  private boolean isValidPassword(String password) {
    if(password == null || password.isEmpty() || password.isBlank()) {
      return false;
    }

    return Pattern.matches(PASSWORD_PATTERN, password);
  }
}
