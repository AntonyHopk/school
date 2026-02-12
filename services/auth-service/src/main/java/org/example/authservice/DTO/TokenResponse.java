package org.example.authservice.DTO;

public record TokenResponse(String accessToken, String refreshToken) {
}
