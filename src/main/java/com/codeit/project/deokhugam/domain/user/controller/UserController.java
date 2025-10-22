package com.codeit.project.deokhugam.domain.user.controller;

import com.codeit.project.deokhugam.domain.user.dto.CursorPageResponsePowerUserDto;
import com.codeit.project.deokhugam.domain.user.dto.UserDto;
import com.codeit.project.deokhugam.domain.user.dto.UserLoginRequest;
import com.codeit.project.deokhugam.domain.user.dto.UserRegisterRequest;
import com.codeit.project.deokhugam.domain.user.dto.UserUpdateRequest;
import com.codeit.project.deokhugam.domain.user.service.UserService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

  private final UserService userService;

  @Override
  public ResponseEntity<UserDto> register(UserRegisterRequest request) {
    return null;
  }

  @Override
  public ResponseEntity<UserDto> login(UserLoginRequest request) {
    return null;
  }

  @Override
  public ResponseEntity<UserDto> find(String userId) {
    return null;
  }

  @Override
  public ResponseEntity<Void> softDelete(String userId) {
    return null;
  }

  @Override
  public ResponseEntity<UserDto> update(String id, UserUpdateRequest request) {
    return null;
  }

  @Override
  public ResponseEntity<CursorPageResponsePowerUserDto> findPowerUsers(String period,
      String direction, LocalDate cursor, LocalDate after, Integer limit) {
    return null;
  }

  @Override
  public ResponseEntity<Void> hardDelete(String userId) {
    return null;
  }
}
