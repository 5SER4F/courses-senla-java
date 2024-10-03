package org.uhanov.dto.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import org.uhanov.dto.AgeRatingDTO;
import org.uhanov.model.AgeRating;

@Mapper(componentModel = "spring")
@Component
public interface AgeRatingMapper {

    AgeRating toModel(AgeRatingDTO ageRatingDTO);

    @Mapping(target = "lastChanger", ignore = true)
    AgeRatingDTO toShortDto(AgeRating ageRating);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAgeRating(AgeRatingDTO dto, @MappingTarget AgeRating entity);
}
