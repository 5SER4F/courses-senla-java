package org.uhanov.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import org.uhanov.dto.UserAuthDTO;
import org.uhanov.dto.UserFullDTO;
import org.uhanov.model.User;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    @Mapping(target = "balance", defaultValue = "0.0")
    User authToModel(UserAuthDTO dto);

    UserFullDTO toFullDto(User user);

    UserAuthDTO toAuthDto(User user);
}
