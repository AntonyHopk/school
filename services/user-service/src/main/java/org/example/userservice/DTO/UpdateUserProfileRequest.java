package org.example.userservice.DTO;

public record UpdateUserProfileRequest(String username, String firstName, String lastName, String bio, String avatarUrl) {
}
