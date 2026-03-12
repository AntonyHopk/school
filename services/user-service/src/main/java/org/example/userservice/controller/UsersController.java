package org.example.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.userservice.DTO.CreateUserProfileRequest;
import org.example.userservice.DTO.UpdateUserProfileRequest;
import org.example.userservice.DTO.UserProfileResponse;
import org.example.userservice.service.UserProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}

