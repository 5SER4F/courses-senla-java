package org.uhanov.dto.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import org.uhanov.dto.staff.StaffFullDto;
import org.uhanov.dto.staff.StaffPostDto;
import org.uhanov.dto.staff.StaffShortDto;
import org.uhanov.model.Staff;

@Mapper(componentModel = "spring")
@Component
public interface StaffMapper {

    Staff authToModel(StaffPostDto dto);

    StaffFullDto toFullDto(Staff dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStaff(StaffPostDto dto, @MappingTarget Staff entity);

}
