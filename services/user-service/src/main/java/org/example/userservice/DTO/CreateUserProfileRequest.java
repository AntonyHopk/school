package org.example.userservice.DTO;

public record CreateUserProfileRequest(Long id, String username, String firstName, String lastName, String bio, String avatarUrl) {
}
