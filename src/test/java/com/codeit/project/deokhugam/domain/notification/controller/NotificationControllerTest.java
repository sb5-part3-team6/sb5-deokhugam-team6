package com.codeit.project.deokhugam.domain.notification.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codeit.project.deokhugam.domain.notification.dto.response.NotificationDto;
import com.codeit.project.deokhugam.domain.notification.dto.request.NotificationUpdateRequest;
import com.codeit.project.deokhugam.domain.notification.service.NotificationService;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private NotificationService notificationService;

  @MockitoBean
  private JpaMetamodelMappingContext jpaMetamodelMappingContext;

  private NotificationDto sampleNotification;
  private PageResponse samplePageResponse;

  @BeforeEach
  void setup() {
    sampleNotification = new NotificationDto("1", "1", "101",
        "Clean Code", "좋은 책이에요", true,
        LocalDateTime.now()
                     .minusDays(1), LocalDateTime.now());

    samplePageResponse = new PageResponse(List.of(sampleNotification),
        "nextCursor", "1", 1, 1L, true);
  }

  @Test
  @DisplayName("GET /api/notifications - 정상 조회")
  void getNotifications_success() throws Exception {
    when(notificationService.getByCursor(anyString(), anyString(), nullable(String.class),
        nullable(String.class), anyInt())).thenReturn(samplePageResponse);

    mockMvc.perform(get("/api/notifications").queryParam("userId", "1")
                                             .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.content[0].id").value(sampleNotification.id()))
           .andExpect(jsonPath("$.content[0].userId").value(sampleNotification.userId()))
           .andExpect(jsonPath("$.content[0].reviewId").value(sampleNotification.reviewId()))
           .andExpect(jsonPath("$.content[0].reviewTitle").value(sampleNotification.reviewTitle()))
           .andExpect(jsonPath("$.content[0].content").value(sampleNotification.content()))
           .andExpect(jsonPath("$.content[0].confirmed").value(sampleNotification.confirmed()))
           .andExpect(jsonPath("$.content[0].createdAt").exists())
           .andExpect(jsonPath("$.content[0].updatedAt").exists())
           .andExpect(jsonPath("$.nextCursor").exists())
           .andExpect(jsonPath("$.hasNext").value(true))
           .andExpect(jsonPath("$.size").value(1))
           .andExpect(jsonPath("$.totalElements").value(1L));
  }

  @Test
  @DisplayName("PATCH /api/notifications/{id} - 단일 알림 확인")
  void checkNotificationById_success() throws Exception {
    when(
        notificationService.checkById(anyString(), any(NotificationUpdateRequest.class),
            anyString())).thenReturn(sampleNotification);

    NotificationUpdateRequest request = new NotificationUpdateRequest(true);

    mockMvc.perform(
               patch("/api/notifications/{notificationId}", "1").header("Deokhugam-Request-User-ID",
                                                                    "user-1")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper.writeValueAsString(
                                                                    request)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(sampleNotification.id()))
           .andExpect(jsonPath("$.userId").value(sampleNotification.userId()))
           .andExpect(jsonPath("$.reviewId").value(sampleNotification.reviewId()))
           .andExpect(jsonPath("$.reviewTitle").value(sampleNotification.reviewTitle()))
           .andExpect(jsonPath("$.content").value(sampleNotification.content()))
           .andExpect(jsonPath("$.confirmed").value(sampleNotification.confirmed()))
           .andExpect(jsonPath("$.createdAt").exists())
           .andExpect(jsonPath("$.updatedAt").exists());
  }

  @Test
  @DisplayName("PATCH /api/notifications/{id} - 헤더 누락")
  void checkNotificationById_missingHeader() throws Exception {
    NotificationUpdateRequest request = new NotificationUpdateRequest(true);

    mockMvc.perform(
               patch("/api/notifications/{notificationId}", "1").contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper.writeValueAsString(
                                                                    request)))
           .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("PATCH /api/notifications/read-all - 전체 확인")
  void checkAllNotification_success() throws Exception {
    mockMvc.perform(
               patch("/api/notifications/read-all").header("Deokhugam-Request-User-ID", "user-1"))
           .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("PATCH /api/notifications/read-all - 헤더 누락")
  void checkAllNotification_missingHeader() throws Exception {
    mockMvc.perform(patch("/api/notifications/read-all"))
           .andExpect(status().isBadRequest());
  }
}