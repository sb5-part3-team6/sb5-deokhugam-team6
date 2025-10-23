package com.codeit.project.deokhugam.domain.book.repository;

import com.codeit.project.deokhugam.domain.book.entity.Book;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book,Long> {
  Boolean existsByisbn(String isbn);

  @Query("""
      select b
      from Book b
      where b.id = :bookId
      and b.deletedAt IS NULL
      """)
  Optional<Book> findById(@Param("bookId")Long bookId);

}
