package org.uhanov.dto.mapper;

import org.mapstruct.Mapper;
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

}
