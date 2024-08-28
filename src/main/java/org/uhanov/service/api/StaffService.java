package org.uhanov.service.api;

import org.uhanov.dto.StaffAuthDTO;
import org.uhanov.dto.StaffFullDTO;

import java.util.UUID;

public interface StaffService {

    StaffFullDTO create(StaffAuthDTO dto);


    StaffFullDTO getById(UUID uuid);


    void update(StaffAuthDTO dto);


    boolean delete(UUID uuid);

}
