package com.interswitch.lms.repository;

import com.interswitch.lms.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    List<Token> findAllByAuthId(Long authId);

    List<Token> findAllByAuthIdAndExpiredFalseAndRevokedFalse(Long authId);
}