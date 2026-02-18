package org.example.authservice.repository;

import org.example.authservice.entity.RefreshToken;
import org.example.authservice.entity.UsersAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
        Optional<RefreshToken> findByTokenHash(String tokenHash);
}
