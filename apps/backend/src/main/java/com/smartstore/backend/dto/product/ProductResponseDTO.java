package com.smartstore.backend.dto.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String category,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
