package com.smartstore.backend.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequestDTO(

        @NotBlank(message = "El refresh token es obligatorio")
        String refreshToken

) {
}
