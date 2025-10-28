package com.codeit.project.deokhugam.domain.user.repository;

import com.codeit.project.deokhugam.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

  boolean existsByEmail(String email);

  boolean existsByNickname(String nickname);

  Optional<User> findByEmail(String email);
}
