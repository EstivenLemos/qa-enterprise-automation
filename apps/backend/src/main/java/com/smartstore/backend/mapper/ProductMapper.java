package com.smartstore.backend.mapper;

import com.smartstore.backend.dto.product.ProductRequestDTO;
import com.smartstore.backend.dto.product.ProductResponseDTO;
import com.smartstore.backend.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "category", source = "category.name")
    ProductResponseDTO toDTO(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductRequestDTO dto);

}
