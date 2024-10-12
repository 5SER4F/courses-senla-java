package org.uhanov.service.api;

import org.uhanov.dto.staff.StaffAuthDto;
import org.uhanov.dto.staff.StaffFullDto;

import java.util.UUID;

public interface StaffService {

    StaffFullDto create(StaffAuthDto dto);


    StaffFullDto getById(UUID uuid);


    void update(StaffAuthDto dto);


    void delete(UUID uuid);

}
