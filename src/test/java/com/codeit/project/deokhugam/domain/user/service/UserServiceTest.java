package com.codeit.project.deokhugam.domain.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codeit.project.deokhugam.domain.user.dto.UserLoginRequest;
import com.codeit.project.deokhugam.domain.user.dto.UserRegisterRequest;
import com.codeit.project.deokhugam.domain.user.entity.User;
import com.codeit.project.deokhugam.domain.user.repository.UserRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Test
  @DisplayName("회원가입 실패 - 중복된 이메일")
  void createUser_Fail_DuplicateEmail() {
    UserRegisterRequest request = new UserRegisterRequest("test@example.com", "testuser", "Password123");

    when(userRepository.existsByEmail(request.email())).thenReturn(true);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      userService.create(request);
    });

    assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  @DisplayName("회원가입 실패 - 잘못된 이메일 형식")
  void createUser_Fail_InvalidEmailFormat() {
    UserRegisterRequest request = new UserRegisterRequest("invalid-email", "testuser", "Password123");

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      userService.create(request);
    });

    assertEquals("유효하지 않은 이메일 형식입니다.", exception.getMessage());
    verify(userRepository, never()).existsByEmail(anyString());
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  @DisplayName("회원가입 실패 - 중복된 닉네임")
  void createUser_Fail_DuplicateNickname() {
    UserRegisterRequest request = new UserRegisterRequest("test@example.com", "testuser", "Password123");

    when(userRepository.existsByEmail(request.email())).thenReturn(false);
    when(userRepository.existsByNickname(request.nickname())).thenReturn(true);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      userService.create(request);
    });

    assertEquals("이미 존재하는 닉네임입니다.", exception.getMessage());
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  @DisplayName("회원가입 실패 - 닉네임 길이 너무 짧음")
  void createUser_Fail_NicknameTooShort() {
    UserRegisterRequest request = new UserRegisterRequest("test@example.com", "a", "Password123");

    when(userRepository.existsByEmail(anyString())).thenReturn(false);
    when(userRepository.existsByNickname(anyString())).thenReturn(false);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      userService.create(request);
    });

    assertEquals("이미 존재하는 닉네임입니다.", exception.getMessage()); // 현재 로직에서는 닉네임 길이와 중복 예외 메시지가 동일
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  @DisplayName("회원가입 실패 - 잘못된 비밀번호 형식")
  void createUser_Fail_InvalidPasswordFormat() {
    UserRegisterRequest request = new UserRegisterRequest("test@example.com", "testuser", "12345");

    when(userRepository.existsByEmail(anyString())).thenReturn(false);
    when(userRepository.existsByNickname(anyString())).thenReturn(false);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      userService.create(request);
    });

    assertEquals("잘못된 비밀번호 형식입니다.", exception.getMessage());
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  @DisplayName("로그인 실패 - 잘못된 이메일 형식")
  void login_Fail_InvalidEmailFormat() {
    // given
    UserLoginRequest request = new UserLoginRequest("invalid-email", "password");

    // when & then
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      userService.login(request);
    });

    assertEquals("유효하지 않은 이메일 형식입니다.", exception.getMessage());

    // 유효성 검사에서 실패했으므로, DB를 조회하는 findByEmail은 절대 호출되면 안 됨
    verify(userRepository, never()).findByEmail(anyString());
  }

  @Test
  @DisplayName("로그인 실패 - 존재하지 않는 이메일")
  void login_Fail_UserNotFound() {
    UserLoginRequest request = new UserLoginRequest("nouser@example.com", "Password123");

    when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

    NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
      userService.login(request);
    });

    assertEquals("존재하지 않는 이메일입니다.", exception.getMessage());
  }

  @Test
  @DisplayName("로그인 실패 - 비밀번호 불일치")
  void login_Fail_IncorrectPassword() {
    UserLoginRequest request = new UserLoginRequest("test@example.com", "WrongPassword123");
    User userInDb = new User("test@example.com", "testuser", "CorrectPassword123");

    when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(userInDb));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      userService.login(request);
    });

    assertEquals("이메일 또는 비밀번호를 확인해주세요.", exception.getMessage());
  }
}