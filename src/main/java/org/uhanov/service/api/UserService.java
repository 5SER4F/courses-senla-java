package org.uhanov.service.api;

import org.uhanov.dto.UserAuthDTO;
import org.uhanov.dto.UserFullDTO;

import java.util.UUID;


public interface UserService {

    UserFullDTO create(UserAuthDTO dto);

    UserFullDTO getById(UUID uuid);

    void update(UserAuthDTO dto);

    boolean delete(UUID uuid);
}
