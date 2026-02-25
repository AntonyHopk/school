package org.example.userservice.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.userservice.DTO.CreateUserProfileRequest;
import org.example.userservice.DTO.UpdateUserProfileRequest;
import org.example.userservice.DTO.UserProfileResponse;
import org.example.userservice.service.UserProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final UserProfileService userProfileService;

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userProfileService.getById(id));
    }

    @GetMapping
    public List<UserProfileResponse> getUserProfiles() {
        return userProfileService.findAll();
    }

    @PostMapping
    public ResponseEntity<UserProfileResponse> create(@RequestBody CreateUserProfileRequest createUserProfileRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userProfileService.create(createUserProfileRequest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchUserProfile(@PathVariable Long id, @RequestBody UpdateUserProfileRequest updateUserProfileRequest) {
        return ResponseEntity.ok(userProfileService.update(id, updateUserProfileRequest));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserProfile(@PathVariable Long id) {
        userProfileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

