package com.smartstore.backend.dto.auth;

public record AuthResponseDTO(

        String accessToken,

        String refreshToken

) {
}