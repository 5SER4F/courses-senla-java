package org.uhanov.dto.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.uhanov.dto.StaffAuthDTO;
import org.uhanov.dto.StaffFullDTO;
import org.uhanov.model.Staff;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-25T10:59:21+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20 (Oracle Corporation)"
)
@Component
public class StaffMapperImpl implements StaffMapper {

    @Override
    public Staff authToModel(StaffAuthDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Staff.StaffBuilder staff = Staff.builder();

        staff.id( dto.getId() );
        staff.password( dto.getPassword() );
        staff.firstname( dto.getFirstname() );
        staff.surname( dto.getSurname() );
        staff.birthDate( dto.getBirthDate() );
        staff.registrationDate( dto.getRegistrationDate() );

        return staff.build();
    }

    @Override
    public StaffAuthDTO toAuthDto(Staff staff) {
        if ( staff == null ) {
            return null;
        }

        StaffAuthDTO.StaffAuthDTOBuilder staffAuthDTO = StaffAuthDTO.builder();

        staffAuthDTO.id( staff.getId() );
        staffAuthDTO.password( staff.getPassword() );
        staffAuthDTO.firstname( staff.getFirstname() );
        staffAuthDTO.surname( staff.getSurname() );
        staffAuthDTO.birthDate( staff.getBirthDate() );
        staffAuthDTO.registrationDate( staff.getRegistrationDate() );

        return staffAuthDTO.build();
    }

    @Override
    public StaffFullDTO toFullDTO(Staff dto) {
        if ( dto == null ) {
            return null;
        }

        StaffFullDTO.StaffFullDTOBuilder staffFullDTO = StaffFullDTO.builder();

        staffFullDTO.id( dto.getId() );
        staffFullDTO.firstname( dto.getFirstname() );
        staffFullDTO.surname( dto.getSurname() );
        staffFullDTO.birthDate( dto.getBirthDate() );
        staffFullDTO.registrationDate( dto.getRegistrationDate() );

        return staffFullDTO.build();
    }

    @Override
    public void updateStaff(StaffAuthDTO dto, Staff entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getPassword() != null ) {
            entity.setPassword( dto.getPassword() );
        }
        if ( dto.getFirstname() != null ) {
            entity.setFirstname( dto.getFirstname() );
        }
        if ( dto.getSurname() != null ) {
            entity.setSurname( dto.getSurname() );
        }
        if ( dto.getBirthDate() != null ) {
            entity.setBirthDate( dto.getBirthDate() );
        }
        if ( dto.getRegistrationDate() != null ) {
            entity.setRegistrationDate( dto.getRegistrationDate() );
        }
    }
}
