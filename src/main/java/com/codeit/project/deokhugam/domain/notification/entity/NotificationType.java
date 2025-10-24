package com.codeit.project.deokhugam.domain.notification.entity;

public enum NotificationType {
  REVIEW_LIKED("[%s]님이 나의 리뷰를 좋아합니다."),
  REVIEW_COMMENTED("[%s]님이 나의 리뷰에 댓글을 남겼습니다."),
  REVIEW_RANKED("리뷰가 인기 순위에 들었어요."),
  ;

  private final String contentFormat;

  NotificationType(String contentFormat) {
    this.contentFormat = contentFormat;
  }

  public String formatContent(Object... args) {
    return String.format(contentFormat, args);
  }
}