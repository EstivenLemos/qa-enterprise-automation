package com.smartstore.backend.mapper;

import com.smartstore.backend.dto.user.UserResponseDTO;
import com.smartstore.backend.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toDTO(User user);

}
