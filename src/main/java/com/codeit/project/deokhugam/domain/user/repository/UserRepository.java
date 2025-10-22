package com.codeit.project.deokhugam.domain.user.repository;

import com.codeit.project.deokhugam.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
