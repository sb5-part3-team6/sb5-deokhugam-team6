package com.codeit.project.deokhugam.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codeit.project.deokhugam.domain.user.dto.response.UserDto;
import com.codeit.project.deokhugam.domain.user.dto.request.UserLoginRequest;
import com.codeit.project.deokhugam.domain.user.dto.request.UserRegisterRequest;
import com.codeit.project.deokhugam.domain.user.dto.request.UserUpdateRequest;
import com.codeit.project.deokhugam.domain.user.entity.User;
import com.codeit.project.deokhugam.domain.user.exception.detail.DeleteNotAllowedException;
import com.codeit.project.deokhugam.domain.user.exception.detail.EmailDuplicationException;
import com.codeit.project.deokhugam.domain.user.exception.detail.LoginInputInvalidException;
import com.codeit.project.deokhugam.domain.user.exception.detail.NicknameDuplicationException;
import com.codeit.project.deokhugam.domain.user.exception.detail.UserAlreadyDeletedException;
import com.codeit.project.deokhugam.domain.user.exception.detail.UserNotFoundException;
import com.codeit.project.deokhugam.domain.user.repository.UserRepository;
import com.codeit.project.deokhugam.domain.user.util.PasswordUtil;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  private UserServiceImpl userService;

  @Mock
  private UserRepository userRepository;

  @Test
  @DisplayName("회원가입 실패 - 중복된 이메일")
  void createUser_Fail_DuplicateEmail() {
    UserRegisterRequest request = new UserRegisterRequest("test@example.com", "testuser",
        "Password123");

    when(userRepository.existsByEmail(request.email())).thenReturn(true);

    EmailDuplicationException exception = assertThrows(EmailDuplicationException.class, () -> {
      userService.create(request);
    });

    assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  @DisplayName("회원가입 실패 - 중복된 닉네임")
  void createUser_Fail_DuplicateNickname() {
    UserRegisterRequest request = new UserRegisterRequest("test@example.com", "testuser",
        "Password123");

    when(userRepository.existsByEmail(request.email())).thenReturn(false);
    when(userRepository.existsByNickname(request.nickname())).thenReturn(true);

    NicknameDuplicationException exception = assertThrows(NicknameDuplicationException.class, () -> {
      userService.create(request);
    });

    assertEquals("이미 존재하는 닉네임입니다.", exception.getMessage());
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  @DisplayName("회원가입 성공 - 특수문자 포함")
  void createUser_Success_WithSpecialChar() {
    UserRegisterRequest request = new UserRegisterRequest("special@example.com", "specialUser",
        "Password!23");

    when(userRepository.existsByEmail(request.email())).thenReturn(false);
    when(userRepository.existsByNickname(request.nickname())).thenReturn(false);

    User savedUserMock = mock(User.class);
    when(savedUserMock.getId()).thenReturn(2L);
    when(savedUserMock.getEmail()).thenReturn(request.email());
    when(savedUserMock.getNickname()).thenReturn(request.nickname());
    when(savedUserMock.getCreatedAt()).thenReturn(LocalDateTime.now());

    when(userRepository.save(any(User.class))).thenReturn(savedUserMock);

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

    UserDto userDto = userService.create(request);

    assertNotNull(userDto);
    assertEquals("2", userDto.id());
    assertEquals(request.email(), userDto.email());
    assertEquals(request.nickname(), userDto.nickname());

    verify(userRepository).existsByEmail(request.email());
    verify(userRepository).existsByNickname(request.nickname());
    verify(userRepository).save(userCaptor.capture());

    User capturedUser = userCaptor.getValue();
    String expectedPassword = PasswordUtil.encrypt(request.password());
    assertEquals(expectedPassword, capturedUser.getPassword());
    assertEquals(request.email(), capturedUser.getEmail());
  }

  @Test
  @DisplayName("회원가입 성공")
  void create_Success() {
    UserRegisterRequest request = new UserRegisterRequest("new@example.com", "newUser",
        "Password123");

    when(userRepository.existsByEmail(request.email())).thenReturn(false);
    when(userRepository.existsByNickname(request.nickname())).thenReturn(false);

    User savedUserMock = mock(User.class);
    when(savedUserMock.getId()).thenReturn(1L);
    when(savedUserMock.getEmail()).thenReturn(request.email());
    when(savedUserMock.getNickname()).thenReturn(request.nickname());
    when(savedUserMock.getCreatedAt()).thenReturn(LocalDateTime.now());

    when(userRepository.save(any(User.class))).thenReturn(savedUserMock);

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

    UserDto userDto = userService.create(request);

    assertNotNull(userDto);
    assertEquals("1", userDto.id());
    assertEquals(request.email(), userDto.email());
    assertEquals(request.nickname(), userDto.nickname());

    verify(userRepository).existsByEmail(request.email());
    verify(userRepository).existsByNickname(request.nickname());
    verify(userRepository).save(userCaptor.capture());

    User capturedUser = userCaptor.getValue();
    String expectedPassword = PasswordUtil.encrypt(request.password());
    assertEquals(expectedPassword, capturedUser.getPassword());
    assertEquals(request.email(), capturedUser.getEmail());
  }

  @Test
  @DisplayName("로그인 실패 - 존재하지 않는 이메일")
  void login_Fail_UserNotFound() {
    UserLoginRequest request = new UserLoginRequest("nouser@example.com", "Password123");

    when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

    LoginInputInvalidException exception = assertThrows(LoginInputInvalidException.class, () -> {
      userService.login(request);
    });

    assertEquals("이메일 또는 비밀번호를 확인해주세요.", exception.getMessage());
  }

  @Test
  @DisplayName("로그인 실패 - 비밀번호 불일치")
  void login_Fail_IncorrectPassword() {
    String correctPassword = "CorrectPassword123";
    String encodedPassword = PasswordUtil.encrypt(correctPassword);
    String wrongPassword = "WrongPassword123";

    UserLoginRequest request = new UserLoginRequest("test@example.com", wrongPassword);
    User userInDb = new User("test@example.com", "testuser", encodedPassword);

    when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(userInDb));

    LoginInputInvalidException exception = assertThrows(LoginInputInvalidException.class, () -> {
      userService.login(request);
    });

    assertEquals("이메일 또는 비밀번호를 확인해주세요.", exception.getMessage());
  }

  @Test
  @DisplayName("로그인 성공")
  void login_Success() {
    String rawPassword = "Password123";
    String encodedPassword = PasswordUtil.encrypt(rawPassword);

    UserLoginRequest request = new UserLoginRequest("user@example.com", rawPassword);

    User dbUserMock = mock(User.class);
    when(dbUserMock.getPassword()).thenReturn(encodedPassword);
    when(dbUserMock.getId()).thenReturn(1L);
    when(dbUserMock.getEmail()).thenReturn(request.email());
    when(dbUserMock.getNickname()).thenReturn("dbUser");
    when(dbUserMock.getCreatedAt()).thenReturn(LocalDateTime.now());

    when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(dbUserMock));

    UserDto userDto = userService.login(request);

    assertNotNull(userDto);
    assertEquals("1", userDto.id());
    assertEquals(request.email(), userDto.email());

    verify(userRepository).findByEmail(request.email());
  }

  @Test
  @DisplayName("사용자 조회 실패 - 존재하지 않는 사용자")
  void findById_Fail_UserNotFound() {
    String userId = "99";
    Long userLongId = 99L;

    when(userRepository.findById(userLongId)).thenReturn(Optional.empty());

    UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
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

    UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
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

    NicknameDuplicationException exception = assertThrows(NicknameDuplicationException.class, () -> {
      userService.update(userId, request);
    });

    assertEquals("이미 존재하는 닉네임입니다.", exception.getMessage());
    verify(userRepository).findById(userLongId);
    verify(userRepository).existsByNickname(duplicateNickname);
  }

  @Test
  @DisplayName("사용자 정보 수정 성공")
  void update_Success() {
    String userId = "1";
    Long userLongId = 1L;
    String newNickname = "newNickname";
    UserUpdateRequest request = new UserUpdateRequest(newNickname);

    User dbUserMock = mock(User.class);
    when(userRepository.findById(userLongId)).thenReturn(Optional.of(dbUserMock));

    when(userRepository.existsByNickname(newNickname)).thenReturn(false);

    when(dbUserMock.getId()).thenReturn(userLongId);
    when(dbUserMock.getEmail()).thenReturn("user@example.com");
    when(dbUserMock.getNickname()).thenReturn(newNickname);
    when(dbUserMock.getCreatedAt()).thenReturn(LocalDateTime.now());

    UserDto userDto = userService.update(userId, request);

    assertNotNull(userDto);
    assertEquals(newNickname, userDto.nickname());

    verify(userRepository).findById(userLongId);
    verify(userRepository).existsByNickname(newNickname);
    verify(dbUserMock).updateNickname(newNickname);
  }

  @Test
  @DisplayName("사용자 논리 삭제 실패 - 존재하지 않는 사용자")
  void softDelete_Fail_UserNotFound() {
    String userId = "99";
    Long userLongId = 99L;

    when(userRepository.findById(userLongId)).thenReturn(Optional.empty());

    UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
      userService.softDelete(userId);
    });

    assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
    verify(userRepository).findById(userLongId);
  }

  @Test
  @DisplayName("사용자 논리 삭제 실패 - 이미 삭제된 사용자")
  void softDelete_Fail_AlreadyDeleted() {
    String userId = "1";
    Long userLongId = 1L;

    User mockUser = mock(User.class);
    when(mockUser.getDeletedAt()).thenReturn(LocalDateTime.now());

    when(userRepository.findById(userLongId)).thenReturn(Optional.of(mockUser));

    UserAlreadyDeletedException exception = assertThrows(UserAlreadyDeletedException.class, () -> {
      userService.softDelete(userId);
    });

    assertEquals("이미 삭제된 사용자입니다.", exception.getMessage());
    verify(mockUser, never()).softDelete();
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

    DeleteNotAllowedException exception = assertThrows(DeleteNotAllowedException.class, () -> {
      userService.hardDelete(userId);
    });

    assertEquals("Soft Delete가 수행되지 않았습니다.", exception.getMessage());
    verify(userRepository).findById(userLongId);
  }

  @Test
  @DisplayName("사용자 영구 삭제 실패 - 존재하지 않는 사용자")
  void hardDelete_Fail_UserNotFound() {
    String userId = "99";
    Long userLongId = 99L;

    when(userRepository.findById(userLongId)).thenReturn(Optional.empty());

    UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
      userService.hardDelete(userId);
    });

    assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
    verify(userRepository, never()).delete(any(User.class));
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