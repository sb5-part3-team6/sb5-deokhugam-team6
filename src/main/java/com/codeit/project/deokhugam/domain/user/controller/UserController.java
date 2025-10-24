package com.codeit.project.deokhugam.domain.user.controller;

import com.codeit.project.deokhugam.domain.user.dto.UserDto;
import com.codeit.project.deokhugam.domain.user.dto.UserLoginRequest;
import com.codeit.project.deokhugam.domain.user.dto.UserRegisterRequest;
import com.codeit.project.deokhugam.domain.user.dto.UserUpdateRequest;
import com.codeit.project.deokhugam.domain.user.service.UserService;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

  private final UserService userService;

  @Override
  @PostMapping("/api/users")
  public ResponseEntity<UserDto> register(@Valid @RequestBody UserRegisterRequest request) {
    UserDto response = userService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED)
                         .body(response);
  }

  @Override
  @PostMapping("/api/users/login")
  public ResponseEntity<UserDto> login(@Valid @RequestBody UserLoginRequest request) {
    UserDto response = userService.login(request);
    return ResponseEntity.status(HttpStatus.OK)
                         .body(response);
  }

  @Override
  @GetMapping("/api/users/{userId}")
  public ResponseEntity<UserDto> find(@PathVariable String userId) {
    UserDto response = userService.findById(userId);
    return ResponseEntity.status(HttpStatus.OK)
                         .body(response);
  }

  @Override
  @DeleteMapping("/api/users/{userId}")
  public ResponseEntity<Void> softDelete(@PathVariable String userId) {
    userService.softDelete(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
                         .build();
  }

  @Override
  @PatchMapping("/api/users/{userId}")
  public ResponseEntity<UserDto> update(@PathVariable String userId,
      @Valid @RequestBody UserUpdateRequest request) {
    UserDto response = userService.update(userId, request);
    return ResponseEntity.status(HttpStatus.OK)
                         .body(response);
  }

  @Override
  @GetMapping("/api/users/power")
  public ResponseEntity<PageResponse> findPowerUsers(String period,
      String direction, LocalDate cursor, LocalDate after, Integer limit) {
    return ResponseEntity.status(HttpStatus.OK)
                         .body(userService.getRank());
  }

  @Override
  @DeleteMapping("/api/users/{userId}/hard")
  public ResponseEntity<Void> hardDelete(@PathVariable String userId) {
    userService.hardDelete(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
                         .build();
  }
}
