package com.smartstore.backend.controllers;

import com.smartstore.backend.dto.category.CategoryRequestDTO;
import com.smartstore.backend.dto.category.CategoryResponseDTO;
import com.smartstore.backend.response.ApiResponse;
import com.smartstore.backend.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<List<CategoryResponseDTO>> findAll() {

        return ApiResponse.success(categoryService.findAll());

    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponseDTO> findById(
            @PathVariable Long id
    ) {

        return ApiResponse.success(categoryService.findById(id));

    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> create(
            @Valid @RequestBody CategoryRequestDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Categoría creada",
                        categoryService.create(dto)
                ));

    }

}
