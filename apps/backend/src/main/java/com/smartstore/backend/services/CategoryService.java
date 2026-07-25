package com.smartstore.backend.services;

import com.smartstore.backend.dto.category.CategoryRequestDTO;
import com.smartstore.backend.dto.category.CategoryResponseDTO;
import com.smartstore.backend.entities.Category;
import com.smartstore.backend.exceptions.ResourceNotFoundException;
import com.smartstore.backend.mapper.CategoryMapper;
import com.smartstore.backend.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public List<CategoryResponseDTO> findAll() {

        return categoryRepository
                .findAll()
                .stream()
                .map(categoryMapper::toDTO)
                .toList();

    }

    public CategoryResponseDTO findById(Long id) {

        Category category = findEntityById(id);

        return categoryMapper.toDTO(category);

    }

    public CategoryResponseDTO create(CategoryRequestDTO dto) {

        Category category = categoryMapper.toEntity(dto);

        categoryRepository.save(category);

        return categoryMapper.toDTO(category);

    }

    private Category findEntityById(Long id) {

        return categoryRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Categoría no encontrada con id: " + id
                        )
                );

    }

}
