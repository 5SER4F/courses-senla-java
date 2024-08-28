package org.uhanov.service.api;

import org.uhanov.dto.AgeRatingDTO;

import java.util.UUID;

public interface AgeRatingService {

    AgeRatingDTO create(AgeRatingDTO ageRatingDTO);


    AgeRatingDTO getById(UUID uuid);


    void update(AgeRatingDTO ageRatingDTO);


    boolean delete(UUID uuid);
}
