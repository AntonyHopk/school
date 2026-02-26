package org.example.courseservice.security;

public record RequestPrincipal(Long userId, String role) {
}
