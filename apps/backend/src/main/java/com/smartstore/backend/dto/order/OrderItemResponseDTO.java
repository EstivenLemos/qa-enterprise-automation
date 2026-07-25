package com.smartstore.backend.dto.order;

import java.math.BigDecimal;

public record OrderItemResponseDTO(

        Long id,

        String productName,

        Integer quantity,

        BigDecimal price

) {
}
