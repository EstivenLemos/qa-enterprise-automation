package com.smartstore.backend.controllers;

import com.smartstore.backend.dto.product.ProductRequestDTO;
import com.smartstore.backend.dto.product.ProductResponseDTO;
import com.smartstore.backend.response.ApiResponse;
import com.smartstore.backend.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<List<ProductResponseDTO>> findAll() {

        return ApiResponse.success(productService.findAll());

    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponseDTO> findById(
            @PathVariable Long id
    ) {

        return ApiResponse.success(productService.findById(id));

    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDTO>> create(
            @Valid @RequestBody ProductRequestDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Producto creado",
                        productService.create(dto)
                ));

    }

}
