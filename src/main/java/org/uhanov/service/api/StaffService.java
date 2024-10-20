package org.uhanov.service.api;

import org.uhanov.dto.staff.StaffSignUpDto;
import org.uhanov.dto.staff.StaffFullDto;

import java.util.UUID;

public interface StaffService {

    StaffFullDto create(StaffSignUpDto dto);


    StaffFullDto getById(UUID uuid);


    StaffFullDto update(StaffSignUpDto dto);


    void delete(UUID uuid);

}
