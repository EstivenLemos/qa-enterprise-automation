package com.smartstore.backend.mapper;

import com.smartstore.backend.dto.order.OrderResponseDTO;
import com.smartstore.backend.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(target = "userEmail", source = "user.email")
    OrderResponseDTO toDTO(Order order);

}
