package org.example.userservice.kafka;

public record AuthUserRegisteredPayload(
        Long userId,
        String email,
        String role
) {
}
