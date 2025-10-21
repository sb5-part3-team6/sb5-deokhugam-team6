package com.codeit.project.deokhugam.service.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codeit.project.deokhugam.domain.notification.dto.NotificationUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NotificationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("GET /api/notifications - 유효한 요청 파라미터로 호출 성공")
  void getNotifications_success() throws Exception {
    mockMvc.perform(get("/api/notifications")
               .param("userId", "1")
               .param("direction", "DESC")
               .param("cursor", "10")
               .param("limit", "20")
               .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.content").exists())
           .andExpect(jsonPath("$.nextCursor").exists())
           .andExpect(jsonPath("$.nextAfter").exists())
           .andExpect(jsonPath("$.size").exists())
           .andExpect(jsonPath("$.totalElements").exists())
           .andExpect(jsonPath("$.hasNext").exists())
           .andExpect(jsonPath("$.content[0].id").exists())
           .andExpect(jsonPath("$.content[0].userId").exists())
           .andExpect(jsonPath("$.content[0].reviewId").exists())
           .andExpect(jsonPath("$.content[0].reviewTitle").exists())
           .andExpect(jsonPath("$.content[0].content").exists())
           .andExpect(jsonPath("$.content[0].confirmed").exists())
           .andExpect(jsonPath("$.content[0].createdAt").exists())
           .andExpect(jsonPath("$.content[0].updatedAt").exists());
  }

  @Test
  @DisplayName("GET /api/notifications - 잘못된 after 파라미터 형식으로 실패")
  void getNotifications_invalidAfterParam() throws Exception {
    mockMvc.perform(get("/api/notifications")
               .param("userId", "1")
               .param("direction", "DESC")
               .param("cursor", "10")
               .param("limit", "20")
               .param("after", "not-a-date")
               .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isBadRequest()); // 400 정상
  }

  @Test
  @DisplayName("PATCH /api/notifications/{id} - 유효한 요청으로 호출 성공")
  void updateNotificationById_success() throws Exception {
    NotificationUpdateRequest request = new NotificationUpdateRequest(true);

    mockMvc.perform(patch("/api/notifications/{notificationId}", "1")
               .contentType(MediaType.APPLICATION_JSON)
               .header("Deokhugam-Request-User-ID", "1")
               .content(objectMapper.writeValueAsString(request)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").exists())
           .andExpect(jsonPath("$.userId").exists())
           .andExpect(jsonPath("$.reviewId").exists())
           .andExpect(jsonPath("$.reviewTitle").exists())
           .andExpect(jsonPath("$.content").exists())
           .andExpect(jsonPath("$.confirmed").exists())
           .andExpect(jsonPath("$.createdAt").exists())
           .andExpect(jsonPath("$.updatedAt").exists());
  }

  @Test
  @DisplayName("PATCH /api/notifications/{id} - 헤더 누락 시 실패")
  void updateNotificationById_missingHeader() throws Exception {
    NotificationUpdateRequest request = new NotificationUpdateRequest(true);

    mockMvc.perform(patch("/api/notifications/{notificationId}", "1")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(request)))
           .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("PATCH /api/notifications/read-all - 헤더 포함 요청 성공")
  void updateNotificationAll_success() throws Exception {
    mockMvc.perform(patch("/api/notifications/read-all")
               .header("Deokhugam-Request-User-ID", "1"))
           .andExpect(status().isOk());
  }

  @Test
  @DisplayName("PATCH /api/notifications/read-all - 헤더 누락 시 실패")
  void updateNotificationAll_missingHeader() throws Exception {
    mockMvc.perform(patch("/api/notifications/read-all"))
           .andExpect(status().isBadRequest());
  }
}