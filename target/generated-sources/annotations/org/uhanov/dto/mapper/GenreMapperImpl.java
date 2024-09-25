package org.uhanov.dto.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.uhanov.dto.GenreDTO;
import org.uhanov.model.Genre;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-25T10:59:21+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20 (Oracle Corporation)"
)
@Component
public class GenreMapperImpl implements GenreMapper {

    @Override
    public GenreDTO toDto(Genre genre) {
        if ( genre == null ) {
            return null;
        }

        GenreDTO.GenreDTOBuilder genreDTO = GenreDTO.builder();

        genreDTO.id( genre.getId() );
        genreDTO.name( genre.getName() );
        genreDTO.lastChanger( genre.getLastChanger() );

        return genreDTO.build();
    }

    @Override
    public Genre toModel(GenreDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Genre.GenreBuilder genre = Genre.builder();

        genre.id( dto.getId() );
        genre.name( dto.getName() );
        genre.lastChanger( dto.getLastChanger() );

        return genre.build();
    }

    @Override
    public void updateGenre(GenreDTO dto, Genre entity) {
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
