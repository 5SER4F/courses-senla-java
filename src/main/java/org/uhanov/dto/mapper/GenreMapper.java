package org.uhanov.dto.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import org.uhanov.dto.GenreDTO;
import org.uhanov.model.Genre;

@Mapper(componentModel = "spring")
@Component
public interface GenreMapper {
    @Mapping(target = "productsWithGenre", ignore = true)
    GenreDTO toDto(Genre genre);

    Genre toModel(GenreDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateGenre(GenreDTO dto, @MappingTarget Genre entity);
}
