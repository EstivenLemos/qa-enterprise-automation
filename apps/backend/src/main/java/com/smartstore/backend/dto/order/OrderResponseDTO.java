package com.smartstore.backend.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(

        Long id,

        String userEmail,

        BigDecimal total,

        String status,

        List<OrderItemResponseDTO> items,

        LocalDateTime createdAt

) {
}
