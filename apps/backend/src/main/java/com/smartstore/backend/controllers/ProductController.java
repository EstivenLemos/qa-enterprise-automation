package com.smartstore.backend.controllers;

import com.smartstore.backend.dto.product.ProductRequestDTO;
import com.smartstore.backend.dto.product.ProductResponseDTO;
import com.smartstore.backend.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")

@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponseDTO> findAll() {

        return productService.findAll();

    }

    @GetMapping("/{id}")
    public ProductResponseDTO findById(
            @PathVariable Long id
    ) {

        return productService.findById(id);

    }

    @PostMapping
    public ProductResponseDTO create(
            @RequestBody ProductRequestDTO dto
    ) {

        return productService.create(dto);

    }

}