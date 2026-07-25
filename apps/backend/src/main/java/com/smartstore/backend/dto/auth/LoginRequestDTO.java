package com.smartstore.backend.dto.auth;

public record LoginRequestDTO(

        String email,

        String password

) {
}