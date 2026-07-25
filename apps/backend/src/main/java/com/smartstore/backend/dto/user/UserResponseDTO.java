package com.smartstore.backend.dto.user;

import com.smartstore.backend.entities.Role;

import java.time.LocalDateTime;

public record UserResponseDTO(

        Long id,

        String firstName,

        String lastName,

        String email,

        Role role,

        LocalDateTime createdAt

) {
}
