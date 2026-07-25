package com.smartstore.backend.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDTO(

        @NotBlank(message = "El nombre de la categoría es obligatorio")
        String name

) {
}
