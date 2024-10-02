package org.uhanov.dto.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import org.uhanov.dto.CreatorAuthDTO;
import org.uhanov.dto.CreatorDto;
import org.uhanov.model.Creator;

@Mapper(componentModel = "spring")
@Component
public interface CreatorMapper {

    @Mapping(target = "products", ignore = true)
    CreatorDto toShortDto(Creator creator);

    Creator postDTOToModel(CreatorAuthDTO creatorAuthDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCreator(CreatorAuthDTO dto, @MappingTarget Creator entity);


}
