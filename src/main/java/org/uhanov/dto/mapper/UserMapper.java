package org.uhanov.dto.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import org.uhanov.dto.user.UserAuthDto;
import org.uhanov.dto.user.UserFullDto;
import org.uhanov.dto.user.UserShortDto;
import org.uhanov.model.User;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    @Mapping(target = "balance", defaultValue = "0.0")
    User authToModel(UserAuthDto dto);

    UserFullDto toFullDto(User user);

    UserShortDto toShortDto(User user);

    UserAuthDto toAuthDto(User user);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(UserAuthDto dto, @MappingTarget User target);
}
