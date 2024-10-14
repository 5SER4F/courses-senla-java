package org.uhanov.dto.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import org.uhanov.dto.creator.CreatorAuthDto;
import org.uhanov.dto.creator.CreatorDto;
import org.uhanov.model.Creator;

@Mapper(componentModel = "spring")
@Component
public interface CreatorMapper {

    CreatorDto toDto(Creator creator);

    Creator authToModel(CreatorAuthDto creatorAuthDto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCreator(CreatorAuthDto dto, @MappingTarget Creator entity);


}
