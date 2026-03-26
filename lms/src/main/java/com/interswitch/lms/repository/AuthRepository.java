package com.interswitch.lms.repository;

import com.interswitch.lms.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {

    Optional<Auth> findByEmail(String email);

    Optional<Auth> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}