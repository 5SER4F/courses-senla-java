package org.uhanov.dto.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import org.uhanov.dto.user.CustomerFullDto;
import org.uhanov.dto.user.CustomerPostDto;
import org.uhanov.dto.user.CustomerShortDto;
import org.uhanov.model.Customer;

@Mapper(componentModel = "spring")
@Component
public interface CustomerMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Customer authToModel(CustomerPostDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CustomerFullDto toFullDto(Customer customer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CustomerShortDto toShortDto(Customer customer);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(CustomerPostDto dto, @MappingTarget Customer target);
}
