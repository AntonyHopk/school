package org.example.userservice.DTO;

import java.time.Instant;

public record CreateUserProfileRequest(Long id, String username, String firstName, String lastName, String bio, String avatarUrl) {
}
