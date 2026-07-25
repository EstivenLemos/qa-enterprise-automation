package com.smartstore.backend.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequestDTO(

        @NotNull(message = "El usuario es obligatorio")
        Long userId,

        @NotEmpty(message = "El pedido debe tener al menos un producto")
        @Valid
        List<OrderItemRequestDTO> items

) {
}
