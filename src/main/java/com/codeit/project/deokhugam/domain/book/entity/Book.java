package com.codeit.project.deokhugam.domain.book.entity;

import com.codeit.project.deokhugam.global.common.entity.BaseDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book extends BaseDeletableEntity {

  @Column(length = 256)
  private String title;
  @Column(length = 256)
  private String author;
  @Column(length = 1024)
  private String description;
  @Column(length = 256)
  private String publisher;

  private LocalDate publishedAt;
  @Column(length = 1024)
  private String isbn;
  @Column(length = 1024)
  private String thumbnailUrl;

  public void update(
      String newTitle,
      String newAuthor,
      String newDescription,
      String newPublisher,
      LocalDate newpublishedDate,
      String newThumbnailImageUrl) {
    if (newTitle != null && !newTitle.equals(this.title)) {
      this.title = newTitle;
    }
    if (newAuthor != null && !newAuthor.equals(this.author)) {
      this.author = newAuthor;
    }
    if (newDescription != null && !newDescription.equals(this.description)) {
      this.description = newDescription;
    }
    if (newPublisher != null && !newPublisher.equals(this.publisher)) {
      this.publisher = newPublisher;
    }
    if (newpublishedDate != null && !newpublishedDate.equals(this.publishedAt)) {
      this.publishedAt = newpublishedDate;
    }
    if (newThumbnailImageUrl != null && !newThumbnailImageUrl.equals(this.thumbnailUrl)) {
      this.thumbnailUrl = newThumbnailImageUrl;
    }
  }
  public void update(String newThumbnailImageUrl) {
    if (newThumbnailImageUrl != null && !newThumbnailImageUrl.equals(this.thumbnailUrl)) {
      this.thumbnailUrl = newThumbnailImageUrl;
    }
  }
}
