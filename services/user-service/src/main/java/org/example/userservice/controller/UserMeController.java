package org.example.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userservice.DTO.UpdateUserProfileRequest;
import org.example.userservice.security.JwtPrincipalExtractor;
import org.example.userservice.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
public class UserMeController {
    private final UserProfileService userProfileService;
    private final JwtPrincipalExtractor jwtPrincipalExtractor;


    @GetMapping
    public ResponseEntity<?> me(@AuthenticationPrincipal Jwt jwt) {
        Long userId = jwtPrincipalExtractor.fromJwt(jwt).userId();
        return ResponseEntity.ok(userProfileService.getById(userId));
    }

    @PatchMapping
    public ResponseEntity<?> patchMe(@AuthenticationPrincipal Jwt jwt,
                                     @Valid @RequestBody UpdateUserProfileRequest updateUserProfileRequest) {
        Long userId = jwtPrincipalExtractor.fromJwt(jwt).userId();
        return ResponseEntity.ok(userProfileService.update(userId, updateUserProfileRequest));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMe(@AuthenticationPrincipal Jwt jwt) {
        Long userId = jwtPrincipalExtractor.fromJwt(jwt).userId();
        userProfileService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
