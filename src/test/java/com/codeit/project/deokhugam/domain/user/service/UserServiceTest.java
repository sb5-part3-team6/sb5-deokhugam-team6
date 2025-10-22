package com.codeit.project.deokhugam.domain.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codeit.project.deokhugam.domain.user.dto.UserDto;
import com.codeit.project.deokhugam.domain.user.dto.UserLoginRequest;
import com.codeit.project.deokhugam.domain.user.dto.UserRegisterRequest;
import com.codeit.project.deokhugam.domain.user.dto.UserUpdateRequest;
import com.codeit.project.deokhugam.domain.user.entity.User;
import com.codeit.project.deokhugam.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
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

    assertEquals("이미 존재하는 닉네임입니다.", exception.getMessage());
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
    UserLoginRequest request = new UserLoginRequest("invalid-email", "password");

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      userService.login(request);
    });

    assertEquals("유효하지 않은 이메일 형식입니다.", exception.getMessage());

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

  @Test
  @DisplayName("사용자 조회 실패 - 존재하지 않는 사용자")
  void findById_Fail_UserNotFound() {
    String userId = "99";
    Long userLongId = 99L;

    when(userRepository.findById(userLongId)).thenReturn(Optional.empty());

    NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
      userService.findById(userId);
    });

    assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());

    verify(userRepository).findById(userLongId);
  }

  @Test
  @DisplayName("사용자 조회 성공")
  void findById_Success() {
    String userId = "1";
    Long userLongId = 1L;
    LocalDateTime now = LocalDateTime.now();

    User mockUser = mock(User.class);
    when(mockUser.getId()).thenReturn(userLongId);
    when(mockUser.getEmail()).thenReturn("test@example.com");
    when(mockUser.getNickname()).thenReturn("testuser");
    when(mockUser.getCreatedAt()).thenReturn(now);

    when(userRepository.findById(userLongId)).thenReturn(Optional.of(mockUser));

    UserDto userDto = userService.findById(userId);

    assertNotNull(userDto);
    assertEquals(userId, userDto.id());
    assertEquals("test@example.com", userDto.email());
    assertEquals("testuser", userDto.nickname());
    assertEquals(now, userDto.createdAt());

    verify(userRepository).findById(userLongId);
  }

  @Test
  @DisplayName("사용자 정보 수정 실패 - 존재하지 않는 사용자")
  void update_Fail_UserNotFound() {
    String userId = "99";
    Long userLongId = 99L;
    UserUpdateRequest request = new UserUpdateRequest("newNickname");

    when(userRepository.findById(userLongId)).thenReturn(Optional.empty());

    NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
      userService.update(userId, request);
    });

    assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
    verify(userRepository).findById(userLongId);
  }

  @Test
  @DisplayName("사용자 정보 수정 실패 - 닉네임 중복")
  void update_Fail_DuplicateNickname() {
    String userId = "1";
    Long userLongId = 1L;
    String duplicateNickname = "existingNickname";
    UserUpdateRequest request = new UserUpdateRequest(duplicateNickname);

    User mockUser = mock(User.class);
    when(userRepository.findById(userLongId)).thenReturn(Optional.of(mockUser));

    when(userRepository.existsByNickname(duplicateNickname)).thenReturn(true);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      userService.update(userId, request);
    });

    assertEquals("이미 존재하는 닉네임입니다.", exception.getMessage());
    verify(userRepository).findById(userLongId);
    verify(userRepository).existsByNickname(duplicateNickname);
  }

  @Test
  @DisplayName("사용자 정보 수정 실패 - 닉네임 길이 너무 짧음")
  void update_Fail_NicknameTooShort() {
    String userId = "1";
    Long userLongId = 1L;
    String shortNickname = "a";
    UserUpdateRequest request = new UserUpdateRequest(shortNickname);

    User mockUser = mock(User.class);
    when(userRepository.findById(userLongId)).thenReturn(Optional.of(mockUser));

    when(userRepository.existsByNickname(shortNickname)).thenReturn(false);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      userService.update(userId, request);
    });

    assertEquals("이미 존재하는 닉네임입니다.", exception.getMessage());
    verify(userRepository).findById(userLongId);
    verify(userRepository).existsByNickname(shortNickname);

    verify(mockUser, never()).updateNickname(anyString());
  }

  @Test
  @DisplayName("사용자 논리 삭제 실패 - 존재하지 않는 사용자")
  void softDelete_Fail_UserNotFound() {
    String userId = "99";
    Long userLongId = 99L;

    when(userRepository.findById(userLongId)).thenReturn(Optional.empty());

    NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
      userService.softDelete(userId);
    });

    assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
    verify(userRepository).findById(userLongId);
  }

  @Test
  @DisplayName("사용자 논리 삭제 성공")
  void softDelete_Success() {
    String userId = "1";
    Long userLongId = 1L;

    User mockUser = mock(User.class);
    when(mockUser.getDeletedAt()).thenReturn(null);

    when(userRepository.findById(userLongId)).thenReturn(Optional.of(mockUser));

    userService.softDelete(userId);

    verify(userRepository).findById(userLongId);

    verify(mockUser).softDelete();
  }

  @Test
  @DisplayName("사용자 영구 삭제 실패 - Soft Delete가 수행되지 않음")
  void hardDelete_Fail_UserIsActive() {
    String userId = "1";
    Long userLongId = 1L;

    User mockUser = mock(User.class);
    when(mockUser.getDeletedAt()).thenReturn(null);

    when(userRepository.findById(userLongId)).thenReturn(Optional.of(mockUser));

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      userService.hardDelete(userId);
    });

    assertEquals("Soft Delete가 수행되지 않았습니다.", exception.getMessage());
    verify(userRepository).findById(userLongId);
  }

  @Test
  @DisplayName("사용자 영구 삭제 성공")
  void hardDelete_Success() {
    String userId = "1";
    Long userLongId = 1L;

    User mockUser = mock(User.class);
    when(mockUser.getDeletedAt()).thenReturn(LocalDateTime.now());

    when(userRepository.findById(userLongId)).thenReturn(Optional.of(mockUser));

    userService.hardDelete(userId);

    verify(userRepository).findById(userLongId);

    verify(userRepository).delete(mockUser);
  }
}