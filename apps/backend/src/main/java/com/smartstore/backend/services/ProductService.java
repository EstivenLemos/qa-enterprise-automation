package com.smartstore.backend.services;

import com.smartstore.backend.dto.product.ProductRequestDTO;
import com.smartstore.backend.dto.product.ProductResponseDTO;
import com.smartstore.backend.entities.Category;
import com.smartstore.backend.entities.Product;
import com.smartstore.backend.exceptions.ResourceNotFoundException;
import com.smartstore.backend.mapper.ProductMapper;
import com.smartstore.backend.repositories.CategoryRepository;
import com.smartstore.backend.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ProductMapper productMapper;

    public List<ProductResponseDTO> findAll() {

        return productRepository.findAll()
                .stream()
                .map(productMapper::toDTO)
                .toList();

    }

    public ProductResponseDTO findById(Long id) {

        Product product = findEntityById(id);

        return productMapper.toDTO(product);

    }

    public ProductResponseDTO create(
            ProductRequestDTO dto
    ) {

        Category category = categoryRepository.findById(
                dto.categoryId()
        ).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Categoría no encontrada con id: " + dto.categoryId()
                )
        );

        Product product = productMapper.toEntity(dto);
        product.setCategory(category);

        productRepository.save(product);

        return productMapper.toDTO(product);

    }

    private Product findEntityById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Producto no encontrado con id: " + id
                        )
                );

    }

}
