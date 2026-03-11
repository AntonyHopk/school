package org.example.authservice.config;

import lombok.RequiredArgsConstructor;
import org.example.authservice.entity.UsersAuth;
import org.example.authservice.repository.UserAuthRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BootstrapAdminRunner implements CommandLineRunner {
    private final UserAuthRepository userRepo;
    private final PasswordEncoder encoder;

    @Value("${ADMIN_EMAIL:}")
    private String adminEmail;
    @Value("${ADMIN_PASSWORD:}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (adminEmail == null || adminEmail.isBlank()) {
            return;
        }
        if (adminPassword == null || adminPassword.isBlank()) {
            return;
        }

        userRepo.findByEmail(adminEmail).ifPresentOrElse(
                existing -> {
                    if (!"ADMIN".equals(existing.getRole())) {
                        existing.setRole("ADMIN");
                        userRepo.save(existing);
                    }
                },
                () -> {
                    UsersAuth user = new UsersAuth();
                    user.setEmail(adminEmail);
                    user.setPasswordHash(encoder.encode(adminPassword));
                    user.setRole("ADMIN");
                    user.setBlocked(false);
                    userRepo.save(user);
                }
        );

    }
}
