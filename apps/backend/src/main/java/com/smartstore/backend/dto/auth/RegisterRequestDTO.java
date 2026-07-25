package com.smartstore.backend.dto.auth;

public record RegisterRequestDTO(

        String firstName,

        String lastName,

        String email,

        String password

) {
}