package org.uhanov.dto.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import org.uhanov.dto.GenreDTO;
import org.uhanov.model.Genre;

@Mapper(componentModel = "spring")
@Component
public interface GenreMapper {
    GenreDTO toDto(Genre genre);

    Genre toModel(GenreDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateGenre(GenreDTO dto, @MappingTarget Genre entity);
}
