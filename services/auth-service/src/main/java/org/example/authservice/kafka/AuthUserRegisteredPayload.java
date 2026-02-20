package org.example.authservice.kafka;

public record AuthUserRegisteredPayload(
        Long userId,
        String email,
        String role
) {
}
