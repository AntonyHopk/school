package org.example.userservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.userservice.DTO.CreateUserProfileRequest;
import org.example.userservice.DTO.UpdateUserProfileRequest;
import org.example.userservice.DTO.UserProfileResponse;
import org.example.userservice.entity.UserProfile;
import org.example.userservice.exception.ConflictException;
import org.example.userservice.exception.NotFoundException;
import org.example.userservice.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Transactional
    public UserProfileResponse getById(Long id) {
        UserProfile userProfile = userProfileRepository.findById(id).orElseThrow(() -> new NotFoundException("UserProfile not found"));
        return toResponse(userProfile);
    }

    @Transactional
    public UserProfileResponse create(CreateUserProfileRequest createUserProfileRequest) {
        if (userProfileRepository.existsById(createUserProfileRequest.id())) {
            throw new ConflictException("UserProfile already exists");
        }
        if (userProfileRepository.existsByUsername(createUserProfileRequest.username())) {
            throw new ConflictException("UserProfile already exists");
        }
        UserProfile userProfile = new UserProfile();
        userProfile.setId(createUserProfileRequest.id());
        userProfile.setUsername(createUserProfileRequest.username());
        userProfile.setFirstName(createUserProfileRequest.firstName());
        userProfile.setLastName(createUserProfileRequest.lastName());
        userProfile.setBio(createUserProfileRequest.bio());
        userProfile.setAvatarUrl(createUserProfileRequest.avatarUrl());
        return toResponse(userProfileRepository.save(userProfile));
    }

    @Transactional
    public UserProfileResponse update(Long id, UpdateUserProfileRequest updateUserProfileRequest) {
        UserProfile userProfile = userProfileRepository.findById(id).orElseThrow(() -> new NotFoundException("UserProfile not found"));
        if (updateUserProfileRequest.firstName() != null && updateUserProfileRequest.username().equals(userProfile.getUsername())) {
            if (userProfileRepository.existsByUsername(updateUserProfileRequest.username())) {
                throw new ConflictException("UserProfile already exists");
            }
            userProfile.setUsername(updateUserProfileRequest.username());
        }
        if (updateUserProfileRequest.firstName() != null) {
            userProfile.setFirstName(updateUserProfileRequest.firstName());
        }
        if (updateUserProfileRequest.lastName() != null) {
            userProfile.setLastName(updateUserProfileRequest.lastName());
        }
        if (updateUserProfileRequest.bio() != null) {
            userProfile.setBio(updateUserProfileRequest.bio());
        }
        if (updateUserProfileRequest.avatarUrl() != null) {
            userProfile.setAvatarUrl(updateUserProfileRequest.avatarUrl());
        }
        return toResponse(userProfileRepository.save(userProfile));
    }

    @Transactional
    public void delete(Long id) {
        if (!userProfileRepository.existsById(id)) {
            throw new NotFoundException("UserProfile not found");
        }
        userProfileRepository.deleteById(id);
    }

    private UserProfileResponse toResponse(UserProfile userProfile) {

        return new UserProfileResponse(
                userProfile.getUsername(),
                userProfile.getFirstName(),
                userProfile.getLastName(),
                userProfile.getBio(),
                userProfile.getAvatarUrl(),
                userProfile.getCreatedAt(),
                userProfile.getUpdatedAt()
        );
    }
}
