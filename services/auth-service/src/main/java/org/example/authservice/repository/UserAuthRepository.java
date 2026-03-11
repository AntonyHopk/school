package org.example.authservice.repository;

import org.example.authservice.entity.UsersAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAuthRepository extends JpaRepository<UsersAuth, Long> {
    Optional<UsersAuth> findByEmail(String email);

}
