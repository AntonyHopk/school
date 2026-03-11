package org.example.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.authservice.DTO.ChangeRoleRequest;
import org.example.authservice.service.UserAdminService;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserAdminService userAdminService;

    @PatchMapping("/users/{id}/role")
    public ResponseEntity<Void> changeRole(@AuthenticationPrincipal Jwt jwt,
                                           @PathVariable Long id,
                                           @Valid @RequestBody ChangeRoleRequest request) {
        String role = jwt.getClaimAsString("role");
        if (role == null) {
            return ResponseEntity.badRequest().build();
        }
        userAdminService.changeRole(id, request.role());
        return ResponseEntity.noContent().build();

    }
}
