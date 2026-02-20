package org.example.authservice.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.example.authservice.DTO.TokenResponse;
import org.example.authservice.entity.RefreshToken;
import org.example.authservice.entity.UsersAuth;
import org.example.authservice.kafka.AuthEventsProducer;
import org.example.authservice.repository.RefreshTokenRepository;
import org.example.authservice.repository.UserAuthRepository;
import org.example.authservice.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final AuthEventsProducer authEventsProducer;

    public Long register(String email, String password) {
        if (userAuthRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists!");
        }
        UsersAuth user = new UsersAuth();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole("STUDENT");

        Long id = userAuthRepository.save(user).getId();
        authEventsProducer.userRegistered(id, user.getEmail(), user.getRole());
        return id;

    }

    public TokenResponse login(String email, String password) {
        UsersAuth user = userAuthRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String access = jwtService.generateAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        RefreshToken refreshTokenObj = new RefreshToken();
        refreshTokenObj.setUser(user);
        refreshTokenObj.setTokenHash(refreshToken);
        refreshTokenObj.setExpiresAt(Instant.now().plusSeconds(604800));
        refreshTokenRepository.save(refreshTokenObj);

        return new TokenResponse(access, refreshToken);

    }

    public TokenResponse refreshToken(String refreshToken) {
        Claims claims = jwtService.parse(refreshToken);
        Long userId = Long.parseLong(claims.getSubject());
        RefreshToken refresh = refreshTokenRepository.findByTokenHash(refreshToken).orElseThrow(() -> new IllegalArgumentException("Token revoked"));
        if (refresh.getRevokeAt() != null) {
            throw new IllegalArgumentException("Token is already revoked");
        }
        if (refresh.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Token is expired");
        }
        UsersAuth user = refresh.getUser();
        String access = jwtService.generateAccessToken(userId, user.getRole());
        return new TokenResponse(access, refreshToken);
    }

    public void logout(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByTokenHash(refreshToken).orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (token.getRevokeAt() != null) {
            return;
        }
        token.setRevokeAt(Instant.now());
        refreshTokenRepository.save(token);
    }
}
