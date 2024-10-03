package org.uhanov.service.api;

import org.uhanov.dto.CreatorAuthDTO;
import org.uhanov.dto.CreatorDto;

import java.util.UUID;

public interface CreatorService {
    CreatorDto create(CreatorAuthDTO creatorAuthDTO);

    CreatorDto getById(UUID uuid);

    void update(CreatorAuthDTO creatorAuthDTO);

    boolean delete(UUID uuid);

}
