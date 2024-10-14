package org.uhanov.service.api;

import org.uhanov.dto.agerating.AgeRatingDto;
import org.uhanov.dto.agerating.AgeRatingPostDto;

import java.util.UUID;

public interface AgeRatingService {

    AgeRatingDto create(AgeRatingPostDto ageRatingDto);


    AgeRatingDto getById(UUID uuid);


    AgeRatingDto update(AgeRatingPostDto ageRatingDto);


    void delete(UUID uuid);
}
