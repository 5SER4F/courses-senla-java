package org.uhanov.dto.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.uhanov.dto.GenreDTO;
import org.uhanov.model.Genre;

@Mapper(componentModel = "spring")
@Component
public interface GenreMapper {
    GenreDTO toDto(Genre genre);

    Genre toModel(GenreDTO dto);
}
