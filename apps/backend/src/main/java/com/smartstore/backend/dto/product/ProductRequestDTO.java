package com.smartstore.backend.dto.product;

import java.math.BigDecimal;

public record ProductRequestDTO(
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        Long categoryId
) {
}