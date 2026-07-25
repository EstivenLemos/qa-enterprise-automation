package com.smartstore.backend.repositories;

import com.smartstore.backend.entities.RefreshToken;
import com.smartstore.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken>
    findByToken(String token);

    Optional<RefreshToken>
    findByUser(User user);

    void deleteByUser(User user);

}