package com.smartstore.backend.controllers;

import com.smartstore.backend.dto.category.CategoryResponseDTO;
import com.smartstore.backend.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponseDTO> findAll() {

        return categoryService.findAll();

    }

    @GetMapping("/{id}")
    public CategoryResponseDTO findById(
            @PathVariable Long id
    ) {

        return categoryService.findById(id);

    }

}