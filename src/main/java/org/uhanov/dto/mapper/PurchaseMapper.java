package org.uhanov.dto.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.uhanov.dto.PurchaseDTO;
import org.uhanov.model.Purchase;

@Mapper(componentModel = "spring")
@Component
public interface PurchaseMapper {
    Purchase toModel(PurchaseDTO dto);

    PurchaseDTO toDto(Purchase purchase);
}
