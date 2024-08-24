package org.uhanov.dto.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.uhanov.dto.UserDTO;
import org.uhanov.model.User;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    User toModel(UserDTO dto);

    UserDTO toDto(User user);
}
