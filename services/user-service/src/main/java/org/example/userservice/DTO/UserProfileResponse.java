package org.example.userservice.DTO;

import jakarta.persistence.Column;

import java.time.Instant;

public record UserProfileResponse(

        String username,
        String firstName,
        String lastName,
        String bio,
        String avatarUrl,
        Instant createdAt,
        Instant updatedAt
) {
}
