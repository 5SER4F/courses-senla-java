package org.uhanov.service.api;

import org.uhanov.dto.creator.CreatorSignUpDto;
import org.uhanov.dto.creator.CreatorDto;

import java.util.UUID;

public interface CreatorService {
    CreatorDto create(CreatorSignUpDto creatorSignUpDto);

    CreatorDto getById(UUID uuid);

    CreatorDto update(CreatorSignUpDto creatorSignUpDto);

    void delete(UUID uuid);

}
