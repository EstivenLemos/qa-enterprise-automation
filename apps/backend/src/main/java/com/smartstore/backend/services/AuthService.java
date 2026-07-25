package com.smartstore.backend.services;

import com.smartstore.backend.dto.auth.AuthResponseDTO;
import com.smartstore.backend.dto.auth.LoginRequestDTO;
import com.smartstore.backend.dto.auth.RegisterRequestDTO;
import com.smartstore.backend.entities.RefreshToken;
import com.smartstore.backend.entities.Role;
import com.smartstore.backend.entities.User;
import com.smartstore.backend.exceptions.ResourceAlreadyExistsException;
import com.smartstore.backend.exceptions.ResourceNotFoundException;
import com.smartstore.backend.repositories.RefreshTokenRepository;
import com.smartstore.backend.repositories.UserRepository;
import com.smartstore.backend.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpirationMs;

    public AuthResponseDTO register(RegisterRequestDTO dto) {

        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new ResourceAlreadyExistsException(
                    "Ya existe un usuario registrado con el email: " + dto.email()
            );
        }

        User user = User.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return issueTokens(user);

    }

    public AuthResponseDTO login(LoginRequestDTO dto) {

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        return issueTokens(user);

    }

    public AuthResponseDTO refresh(String refreshToken) {

        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new BadCredentialsException("Refresh token inválido"));

        if (storedToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(storedToken);
            throw new BadCredentialsException("Refresh token expirado");
        }

        String newAccessToken = jwtService.generateAccessToken(storedToken.getUser());

        return new AuthResponseDTO(newAccessToken, refreshToken);

    }

    public void logout(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + email));

        refreshTokenRepository.deleteByUser(user);

    }

    private AuthResponseDTO issueTokens(User user) {

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        RefreshToken tokenEntity = refreshTokenRepository.findByUser(user)
                .orElseGet(() -> RefreshToken.builder().user(user).build());

        tokenEntity.setToken(refreshToken);
        tokenEntity.setExpiryDate(Instant.now().plusMillis(refreshTokenExpirationMs));

        refreshTokenRepository.save(tokenEntity);

        return new AuthResponseDTO(accessToken, refreshToken);

    }

}
