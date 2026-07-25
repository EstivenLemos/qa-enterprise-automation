package com.smartstore.backend.controllers;

import com.smartstore.backend.dto.auth.AuthResponseDTO;
import com.smartstore.backend.dto.auth.LoginRequestDTO;
import com.smartstore.backend.dto.auth.RefreshRequestDTO;
import com.smartstore.backend.dto.auth.RegisterRequestDTO;
import com.smartstore.backend.entities.User;
import com.smartstore.backend.response.ApiResponse;
import com.smartstore.backend.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> register(
            @Valid @RequestBody RegisterRequestDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Usuario registrado",
                        authService.register(dto)
                ));

    }

    @PostMapping("/login")
    public ApiResponse<AuthResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto
    ) {

        return ApiResponse.success(
                "Inicio de sesión exitoso",
                authService.login(dto)
        );

    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponseDTO> refresh(
            @Valid @RequestBody RefreshRequestDTO dto
    ) {

        return ApiResponse.success(
                authService.refresh(dto.refreshToken())
        );

    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(
            @AuthenticationPrincipal User user
    ) {

        authService.logout(user.getEmail());

        return ApiResponse.success("Sesión cerrada", null);

    }

}
