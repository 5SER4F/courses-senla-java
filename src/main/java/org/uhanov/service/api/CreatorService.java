package org.uhanov.service.api;

import org.uhanov.dto.CreatorAuthDTO;
import org.uhanov.dto.CreatorGetFullDto;

import java.util.UUID;

public interface CreatorService {
    CreatorGetFullDto create(CreatorAuthDTO creatorAuthDTO);

    CreatorGetFullDto getById(UUID uuid);

    void update(CreatorAuthDTO creatorAuthDTO);

    boolean delete(UUID uuid);

}
