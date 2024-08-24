package org.uhanov.dto.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.uhanov.dto.CreatorDTO;
import org.uhanov.model.Creator;

@Mapper(componentModel = "spring")
@Component
public interface CreatorMapper {
    CreatorDTO toDto(Creator creator);

    Creator toModel(CreatorDTO creatorDTO);
}
