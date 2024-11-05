package org.uhanov.service.api;

import org.uhanov.dto.staff.StaffFullDto;
import org.uhanov.dto.staff.StaffPostDto;

import java.util.UUID;

public interface StaffService {

    StaffFullDto create(StaffPostDto dto);

    StaffFullDto getById(UUID uuid);

    StaffFullDto update(StaffPostDto dto);

    void delete(UUID uuid);

}
