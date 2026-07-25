package com.smartstore.backend.services;

import com.smartstore.backend.dto.product.ProductRequestDTO;
import com.smartstore.backend.dto.product.ProductResponseDTO;
import com.smartstore.backend.entities.Category;
import com.smartstore.backend.entities.Product;
import com.smartstore.backend.exceptions.ResourceNotFoundException;
import com.smartstore.backend.mapper.ProductMapper;
import com.smartstore.backend.repositories.CategoryRepository;
import com.smartstore.backend.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void findAll_returnsMappedProducts() {

        Product product = Product.builder().id(1L).name("Laptop").build();
        ProductResponseDTO dto = new ProductResponseDTO(
                1L, "Laptop", "desc", BigDecimal.TEN, 5, "Electronics",
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(productRepository.findAll()).thenReturn(List.of(product));
        when(productMapper.toDTO(product)).thenReturn(dto);

        List<ProductResponseDTO> result = productService.findAll();

        assertThat(result).containsExactly(dto);

    }

    @Test
    void findById_whenMissing_throwsResourceNotFound() {

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

    }

    @Test
    void create_whenCategoryMissing_throwsResourceNotFound() {

        ProductRequestDTO request = new ProductRequestDTO(
                "Laptop", "desc", BigDecimal.TEN, 5, 42L
        );

        when(categoryRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("42");

        verify(productRepository, never()).save(any());

    }

    @Test
    void create_savesProductWithResolvedCategory() {

        ProductRequestDTO request = new ProductRequestDTO(
                "Laptop", "desc", BigDecimal.TEN, 5, 1L
        );
        Category category = Category.builder().id(1L).name("Electronics").build();
        Product entity = Product.builder().name("Laptop").description("desc")
                .price(BigDecimal.TEN).stock(5).build();
        ProductResponseDTO responseDTO = new ProductResponseDTO(
                10L, "Laptop", "desc", BigDecimal.TEN, 5, "Electronics",
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productMapper.toEntity(request)).thenReturn(entity);
        when(productMapper.toDTO(entity)).thenReturn(responseDTO);

        ProductResponseDTO result = productService.create(request);

        verify(productRepository).save(entity);
        assertThat(entity.getCategory()).isEqualTo(category);
        assertThat(result).isEqualTo(responseDTO);

    }

}
