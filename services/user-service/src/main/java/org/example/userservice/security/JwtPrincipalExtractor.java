package org.example.userservice.security;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtPrincipalExtractor {
    public JwtPrincipal fromJwt(Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        String role = jwt.getClaimAsString("role");
        return new JwtPrincipal(userId,role);
    }
}
