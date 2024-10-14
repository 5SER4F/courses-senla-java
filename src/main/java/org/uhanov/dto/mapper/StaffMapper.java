package org.uhanov.dto.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import org.uhanov.dto.staff.StaffAuthDto;
import org.uhanov.dto.staff.StaffFullDto;
import org.uhanov.dto.staff.StaffShortDto;
import org.uhanov.model.Staff;

@Mapper(componentModel = "spring")
@Component
public interface StaffMapper {

    Staff authToModel(StaffAuthDto dto);

    StaffShortDto toShortDto(Staff staff);

    StaffFullDto toFullDto(Staff dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStaff(StaffAuthDto dto, @MappingTarget Staff entity);

}
