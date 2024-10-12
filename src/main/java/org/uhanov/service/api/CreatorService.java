package org.uhanov.service.api;

import org.uhanov.dto.creator.CreatorAuthDto;
import org.uhanov.dto.creator.CreatorDto;

import java.util.UUID;

public interface CreatorService {
    CreatorDto create(CreatorAuthDto creatorAuthDto);

    CreatorDto getById(UUID uuid);

    void update(CreatorAuthDto creatorAuthDto);

    void delete(UUID uuid);

}
