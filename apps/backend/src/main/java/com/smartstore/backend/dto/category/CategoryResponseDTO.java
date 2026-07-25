package com.smartstore.backend.dto.category;

import java.time.LocalDateTime;

public record CategoryResponseDTO(

        Long id,

        String name,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {
}
