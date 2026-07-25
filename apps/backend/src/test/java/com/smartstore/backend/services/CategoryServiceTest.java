package com.smartstore.backend.services;

import com.smartstore.backend.dto.category.CategoryRequestDTO;
import com.smartstore.backend.dto.category.CategoryResponseDTO;
import com.smartstore.backend.entities.Category;
import com.smartstore.backend.exceptions.ResourceNotFoundException;
import com.smartstore.backend.mapper.CategoryMapper;
import com.smartstore.backend.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void findAll_returnsMappedCategories() {

        Category category = Category.builder().id(1L).name("Electronics").build();
        CategoryResponseDTO dto = new CategoryResponseDTO(1L, "Electronics", LocalDateTime.now(), LocalDateTime.now());

        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(categoryMapper.toDTO(category)).thenReturn(dto);

        List<CategoryResponseDTO> result = categoryService.findAll();

        assertThat(result).containsExactly(dto);

    }

    @Test
    void findById_whenExists_returnsMappedCategory() {

        Category category = Category.builder().id(1L).name("Electronics").build();
        CategoryResponseDTO dto = new CategoryResponseDTO(1L, "Electronics", LocalDateTime.now(), LocalDateTime.now());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDTO(category)).thenReturn(dto);

        CategoryResponseDTO result = categoryService.findById(1L);

        assertThat(result).isEqualTo(dto);

    }

    @Test
    void findById_whenMissing_throwsResourceNotFound() {

        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

    }

    @Test
    void create_savesAndReturnsMappedCategory() {

        CategoryRequestDTO request = new CategoryRequestDTO("Books");
        Category entity = Category.builder().name("Books").build();
        CategoryResponseDTO responseDTO = new CategoryResponseDTO(2L, "Books", LocalDateTime.now(), LocalDateTime.now());

        when(categoryMapper.toEntity(request)).thenReturn(entity);
        when(categoryMapper.toDTO(entity)).thenReturn(responseDTO);

        CategoryResponseDTO result = categoryService.create(request);

        verify(categoryRepository).save(entity);
        assertThat(result).isEqualTo(responseDTO);

    }

}
