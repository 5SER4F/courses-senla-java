package org.uhanov.dto.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.uhanov.dto.StaffDTO;
import org.uhanov.model.Staff;

@Mapper(componentModel = "spring")
@Component
public interface StaffMapper {
    Staff toModel(StaffDTO dto);

    StaffDTO toDto(Staff staff);
}
