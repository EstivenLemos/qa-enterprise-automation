package com.smartstore.backend.services;

import com.smartstore.backend.dto.category.CategoryResponseDTO;
import com.smartstore.backend.entities.Category;
import com.smartstore.backend.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponseDTO> findAll() {

        return categoryRepository
                .findAll()
                .stream()
                .map(this::toDTO)
                .toList();

    }

    public CategoryResponseDTO findById(Long id) {

        Category category =
                categoryRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Category not found"
                                )
                        );

        return toDTO(category);

    }

    private CategoryResponseDTO toDTO(
            Category category
    ) {

        return new CategoryResponseDTO(
                category.getId(),
                category.getName()
        );

    }

}