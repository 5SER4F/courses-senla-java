package org.uhanov.dto.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import org.uhanov.dto.StaffAuthDTO;
import org.uhanov.dto.StaffFullDTO;
import org.uhanov.model.Staff;

@Mapper(componentModel = "spring")
@Component
public interface StaffMapper {

    Staff authToModel(StaffAuthDTO dto);

    StaffAuthDTO toAuthDto(Staff staff);

    StaffFullDTO toFullDTO(Staff dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStaff(StaffAuthDTO dto, @MappingTarget Staff entity);

}
