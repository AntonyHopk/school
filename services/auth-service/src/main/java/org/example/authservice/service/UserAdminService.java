package org.example.authservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.authservice.entity.UsersAuth;
import org.example.authservice.repository.UserAuthRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserAdminService {
    private static final Set<String> ALLOWED_ROLES = Set.of("STUDENT", "TEACHER","ADMIN");
    private final UserAuthRepository userRepo;

    @Transactional
    public void changeRole(Long userId,String role) {
        String roleNormal = normalizeRole(role);
        UsersAuth user = userRepo.findById(userId).orElseThrow(()->new IllegalArgumentException("User not found"));
        user.setRole(roleNormal);
        userRepo.save(user);

    }

    private static String normalizeRole(String role) {
        if (role == null) { throw new IllegalArgumentException("role cannot be null"); }
        String normalizedRole = role.trim().toUpperCase();
        if (!ALLOWED_ROLES.contains(normalizedRole)) {
            throw new IllegalArgumentException("Invalid role: " + normalizedRole);
        }
        return normalizedRole;
    }
}
