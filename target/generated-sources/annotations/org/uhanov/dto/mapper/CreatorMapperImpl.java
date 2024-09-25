package org.uhanov.dto.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.uhanov.dto.CreatorAuthDTO;
import org.uhanov.dto.CreatorGetFullDto;
import org.uhanov.model.Creator;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-25T10:59:21+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20 (Oracle Corporation)"
)
@Component
public class CreatorMapperImpl implements CreatorMapper {

    @Override
    public CreatorGetFullDto toFullGETDto(Creator creator) {
        if ( creator == null ) {
            return null;
        }

        CreatorGetFullDto.CreatorGetFullDtoBuilder creatorGetFullDto = CreatorGetFullDto.builder();

        creatorGetFullDto.id( creator.getId() );
        creatorGetFullDto.name( creator.getName() );
        creatorGetFullDto.registrationDate( creator.getRegistrationDate() );

        return creatorGetFullDto.build();
    }

    @Override
    public CreatorAuthDTO toPostDTO(Creator creator) {
        if ( creator == null ) {
            return null;
        }

        CreatorAuthDTO.CreatorAuthDTOBuilder creatorAuthDTO = CreatorAuthDTO.builder();

        creatorAuthDTO.id( creator.getId() );
        creatorAuthDTO.password( creator.getPassword() );
        creatorAuthDTO.name( creator.getName() );
        creatorAuthDTO.registrationDate( creator.getRegistrationDate() );

        return creatorAuthDTO.build();
    }

    @Override
    public Creator postDTOToModel(CreatorAuthDTO creatorAuthDTO) {
        if ( creatorAuthDTO == null ) {
            return null;
        }

        Creator.CreatorBuilder creator = Creator.builder();

        creator.id( creatorAuthDTO.getId() );
        creator.password( creatorAuthDTO.getPassword() );
        creator.name( creatorAuthDTO.getName() );
        creator.registrationDate( creatorAuthDTO.getRegistrationDate() );

        return creator.build();
    }

    @Override
    public void updateCreator(CreatorAuthDTO dto, Creator entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getPassword() != null ) {
            entity.setPassword( dto.getPassword() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getRegistrationDate() != null ) {
            entity.setRegistrationDate( dto.getRegistrationDate() );
        }
    }
}
