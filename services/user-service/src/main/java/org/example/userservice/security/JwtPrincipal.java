package org.example.userservice.security;

public record JwtPrincipal(Long userId,String role) {
}
