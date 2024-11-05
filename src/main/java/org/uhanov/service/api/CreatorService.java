package org.uhanov.service.api;

import org.uhanov.dto.creator.CreatorDto;
import org.uhanov.dto.creator.CreatorPostDto;

import java.util.UUID;

public interface CreatorService {
    CreatorDto create(CreatorPostDto creatorPostDto);

    CreatorDto getById(UUID uuid);

    CreatorDto update(CreatorPostDto creatorPostDto);

    void countProfit(UUID creatorId);

}
