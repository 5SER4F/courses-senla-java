package org.uhanov.dto.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import org.uhanov.dto.UserAuthDTO;
import org.uhanov.dto.UserFullDTO;
import org.uhanov.model.User;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    @Mapping(target = "balance", defaultValue = "0.0")
    User authToModel(UserAuthDTO dto);

    //    @Mapping(target = "userPurchase", ignore = true)
    UserFullDTO toFullDto(User user);

    UserAuthDTO toAuthDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(UserAuthDTO dto, @MappingTarget User target);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(User patch, @MappingTarget User target);
}
