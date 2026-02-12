package org.example.authservice.service;

import lombok.RequiredArgsConstructor;
import org.example.authservice.DTO.TokenResponse;
import org.example.authservice.entity.RefreshToken;
import org.example.authservice.entity.UsersAuth;
import org.example.authservice.repository.RefreshTokenRepository;
import org.example.authservice.repository.UserAuthRepository;
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

    public Long register(String email, String password) {
        if (userAuthRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists!");
        }
        UsersAuth user = new UsersAuth();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole("STUDENT");
        return userAuthRepository.save(user).getId();

    }

    public TokenResponse login(String email, String password) {
        UsersAuth user = userAuthRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if(!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String access = jwtService.generateAccessToken(user.getId(),user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        RefreshToken refreshTokenObj = new RefreshToken();
        refreshTokenObj.setUser(user);
        refreshTokenObj.setTokenHash(refreshToken);
        refreshTokenObj.setExpiredAt(Instant.now().plusSeconds(604800));
        refreshTokenRepository.save(refreshTokenObj);

        return new TokenResponse(access,refreshToken);

    }
}
