package org.uhanov.dto.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import org.uhanov.dto.purchase.PurchaseDto;
import org.uhanov.dto.purchase.PurchasePostDto;
import org.uhanov.model.Purchase;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class})
@Component
public interface PurchaseMapper {
    Purchase toModel(PurchasePostDto dto);

    PurchaseDto toDto(Purchase purchase);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePurchase(PurchaseDto dto, @MappingTarget Purchase entity);
}
