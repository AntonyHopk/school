package org.example.courseservice.security;

public record JwtPrincipal(Long userId,String role) {
}
