package org.uhanov.dto.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.uhanov.dto.AgeRatingDTO;
import org.uhanov.model.AgeRating;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-25T10:59:21+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20 (Oracle Corporation)"
)
@Component
public class AgeRatingMapperImpl implements AgeRatingMapper {

    @Override
    public AgeRating toModel(AgeRatingDTO ageRatingDTO) {
        if ( ageRatingDTO == null ) {
            return null;
        }

        AgeRating.AgeRatingBuilder ageRating = AgeRating.builder();

        ageRating.id( ageRatingDTO.getId() );
        ageRating.name( ageRatingDTO.getName() );
        ageRating.lastChanger( ageRatingDTO.getLastChanger() );

        return ageRating.build();
    }

    @Override
    public AgeRatingDTO toDto(AgeRating ageRating) {
        if ( ageRating == null ) {
            return null;
        }

        AgeRatingDTO.AgeRatingDTOBuilder ageRatingDTO = AgeRatingDTO.builder();

        ageRatingDTO.id( ageRating.getId() );
        ageRatingDTO.name( ageRating.getName() );
        ageRatingDTO.lastChanger( ageRating.getLastChanger() );

        return ageRatingDTO.build();
    }

    @Override
    public void updateAgeRating(AgeRatingDTO dto, AgeRating entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getLastChanger() != null ) {
            entity.setLastChanger( dto.getLastChanger() );
        }
    }
}
