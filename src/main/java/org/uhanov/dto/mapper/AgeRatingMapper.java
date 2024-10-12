package org.uhanov.dto.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import org.uhanov.dto.agerating.AgeRatingDto;
import org.uhanov.dto.agerating.AgeRatingPostDto;
import org.uhanov.model.AgeRating;

@Mapper(componentModel = "spring", uses = {StaffMapper.class})
@Component
public interface AgeRatingMapper {

    AgeRating postDtoToModel(AgeRatingPostDto ageRatingPostDto);

    AgeRating toModel(AgeRatingDto ageRatingDto);

    @Mapping(target = "lastChanger", source = "lastChanger")
    AgeRatingDto toDto(AgeRating ageRating);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "lastChanger", ignore = true)
    void updateAgeRating(AgeRatingPostDto dto, @MappingTarget AgeRating entity);

}
