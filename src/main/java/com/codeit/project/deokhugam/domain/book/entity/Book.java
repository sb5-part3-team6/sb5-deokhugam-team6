package com.codeit.project.deokhugam.domain.book.entity;

import com.codeit.project.deokhugam.global.entity.BaseEntity;
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
public class Book extends BaseEntity {

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
}
