package com.smartstore.backend.services;

import com.smartstore.backend.dto.product.ProductRequestDTO;
import com.smartstore.backend.dto.product.ProductResponseDTO;
import com.smartstore.backend.entities.Category;
import com.smartstore.backend.entities.Product;
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

    public List<ProductResponseDTO> findAll() {

        return productRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();

    }

    public ProductResponseDTO findById(Long id) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow();

        return toDTO(product);

    }

    public ProductResponseDTO create(
            ProductRequestDTO dto
    ) {

        Category category =
                categoryRepository.findById(
                        dto.categoryId()
                ).orElseThrow();

        Product product =
                Product.builder()
                        .name(dto.name())
                        .description(dto.description())
                        .price(dto.price())
                        .stock(dto.stock())
                        .category(category)
                        .build();

        productRepository.save(product);

        return toDTO(product);

    }

    private ProductResponseDTO toDTO(
            Product product
    ) {

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory().getName()
        );

    }

}