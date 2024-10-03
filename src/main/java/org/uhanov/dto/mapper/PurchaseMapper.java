package org.uhanov.dto.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import org.uhanov.dto.PurchaseDTO;
import org.uhanov.model.Purchase;

@Mapper(componentModel = "spring")
@Component
public interface PurchaseMapper {
    Purchase toModel(PurchaseDTO dto);

    PurchaseDTO toDto(Purchase purchase);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePurchase(PurchaseDTO dto, @MappingTarget Purchase entity);
}
