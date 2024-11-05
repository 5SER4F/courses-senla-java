package org.uhanov.dto.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import org.uhanov.dto.creator.CreatorDto;
import org.uhanov.dto.creator.CreatorPostDto;
import org.uhanov.model.Creator;

@Mapper(componentModel = "spring")
@Component
public interface CreatorMapper {

    CreatorDto toDto(Creator creator);

    Creator authToModel(CreatorPostDto creatorPostDto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCreator(CreatorPostDto dto, @MappingTarget Creator entity);


}
