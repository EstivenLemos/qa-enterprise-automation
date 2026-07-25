package com.smartstore.backend.mapper;

import com.smartstore.backend.dto.category.CategoryRequestDTO;
import com.smartstore.backend.dto.category.CategoryResponseDTO;
import com.smartstore.backend.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponseDTO toDTO(Category category);

    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryRequestDTO dto);

}
