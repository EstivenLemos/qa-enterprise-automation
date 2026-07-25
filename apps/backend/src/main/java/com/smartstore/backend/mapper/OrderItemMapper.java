package com.smartstore.backend.mapper;

import com.smartstore.backend.dto.order.OrderItemResponseDTO;
import com.smartstore.backend.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "productName", source = "product.name")
    OrderItemResponseDTO toDTO(OrderItem orderItem);

}
