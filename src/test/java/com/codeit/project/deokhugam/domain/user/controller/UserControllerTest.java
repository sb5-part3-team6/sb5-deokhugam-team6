package com.codeit.project.deokhugam.domain.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codeit.project.deokhugam.domain.user.dto.request.UserRegisterRequest;
import com.codeit.project.deokhugam.domain.user.dto.response.UserDto;
import com.codeit.project.deokhugam.domain.user.exception.detail.EmailDuplicationException;
import com.codeit.project.deokhugam.domain.user.exception.detail.NicknameDuplicationException;
import com.codeit.project.deokhugam.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  UserService userService;

  @MockitoBean
  JpaMetamodelMappingContext jpaMetamodelMappingContext;

  @Test
  @DisplayName("회원가입 실패- 400 / 잘못된 이메일 형식")
  void register_failed_invalid_email_format() throws Exception {
    UserRegisterRequest invalidRequest = new UserRegisterRequest(
        "invalid-email-format",
        "testuser",
        "Password123"
    );

    mockMvc.perform(post("/api/users")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(invalidRequest)))
           .andExpect(status().isBadRequest());

    verify(userService, never()).create(any(UserRegisterRequest.class));
  }

  @Test
  @DisplayName("회원가입 실패 - 400 / 빈 닉네임")
  void register_failed_dto_validation() throws Exception {
    UserRegisterRequest invalidRequest = new UserRegisterRequest(
        "test@example.com", "", "Password123"
    );

    ResultActions actions = mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidRequest)));

    actions
        .andExpect(status().isBadRequest())
        .andDo(print());

    verify(userService, never()).create(any(UserRegisterRequest.class));
  }

  @Test
  @DisplayName("회원가입 실패 - 400 / 잘못된 비밀번호 형식")
  void register_failed_invalid_password_format() throws Exception {
    UserRegisterRequest invalidRequest = new UserRegisterRequest(
        "test@example.com", "testuser", "pass123"
    );

    ResultActions actions = mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidRequest)));

    actions
        .andExpect(status().isBadRequest())
        .andDo(print());

    verify(userService, never()).create(any(UserRegisterRequest.class));
  }

  @Test
  @DisplayName("회원가입 실패 - 400 / 중복된 이메일")
  void register_failed_duplicate_email() throws Exception {
    UserRegisterRequest request = new UserRegisterRequest(
        "duplicate@example.com", "testuser", "Password123"
    );

    when(userService.create(any(UserRegisterRequest.class)))
        .thenThrow(new EmailDuplicationException());

    ResultActions actions = mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)));

    actions
        .andExpect(status().isConflict())
        .andDo(print());

    verify(userService, times(1)).create(any(UserRegisterRequest.class));
  }

  @Test
  @DisplayName("회원가입 실패 - 400 / 중복된 닉네임")
  void register_failed_duplicate_nickname() throws Exception {
    UserRegisterRequest validRequest = new UserRegisterRequest(
        "test@example.com",
        "duplicateNickname",
        "Password123"
    );

    when(userService.create(any(UserRegisterRequest.class)))
        .thenThrow(new NicknameDuplicationException().withNickname("duplicateNickname"));

    ResultActions actions = mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(validRequest)));

    actions
        .andExpect(status().isConflict())
        .andDo(print());

    verify(userService, times(1)).create(any(UserRegisterRequest.class));
  }

  @Test
  @DisplayName("회원가입 성공 - 201")
  void register_success() throws Exception {
    UserRegisterRequest request = new UserRegisterRequest(
        "test@example.com",
        "testuser",
        "Password123"
    );

    UserDto mockResponse = new UserDto("1", "test@example.com", "testuser", null);

    when(userService.create(any(UserRegisterRequest.class)))
        .thenReturn(mockResponse);

    mockMvc.perform(post("/api/users")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(request)))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.email").value("test@example.com"))
           .andExpect(jsonPath("$.nickname").value("testuser"));

    verify(userService, times(1)).create(any(UserRegisterRequest.class));
  }
}