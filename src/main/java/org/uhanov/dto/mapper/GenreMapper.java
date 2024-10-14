package org.uhanov.dto.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import org.uhanov.dto.genre.GenreDto;
import org.uhanov.dto.genre.GenrePostDto;
import org.uhanov.model.Genre;

@Mapper(componentModel = "spring", uses = {StaffMapper.class})
@Component
public interface GenreMapper {
    GenreDto toDto(Genre genre);

    Genre toModel(GenrePostDto dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateGenre(GenrePostDto dto, @MappingTarget Genre entity);
}
