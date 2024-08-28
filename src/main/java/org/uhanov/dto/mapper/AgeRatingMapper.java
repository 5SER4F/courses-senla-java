package org.uhanov.dto.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.uhanov.dto.AgeRatingDTO;
import org.uhanov.model.AgeRating;

@Mapper(componentModel = "spring")
@Component
public interface AgeRatingMapper {
    AgeRating toModel(AgeRatingDTO ageRatingDTO);

    AgeRatingDTO toDto(AgeRating ageRating);
}
