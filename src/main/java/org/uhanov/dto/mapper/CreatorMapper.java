package org.uhanov.dto.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.uhanov.dto.CreatorAuthDTO;
import org.uhanov.dto.CreatorGetFullDto;
import org.uhanov.model.Creator;

@Mapper(componentModel = "spring")
@Component
public interface CreatorMapper {
    CreatorGetFullDto toFullGETDto(Creator creator);

    CreatorAuthDTO toPostDTO(Creator creator);

    Creator postDTOToModel(CreatorAuthDTO creatorAuthDTO);


}
