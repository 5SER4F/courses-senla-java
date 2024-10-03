package org.uhanov.dto.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import org.uhanov.dto.ProductDTO;
import org.uhanov.model.Product;

@Mapper(componentModel = "spring")
@Component
public interface ProductMapper {
    Product toModel(ProductDTO dto);

    @Mapping(target = "purchasesWithProduct", ignore = true)
    @Mapping(target = "genres", ignore = true)
    ProductDTO toDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProduct(ProductDTO dto, @MappingTarget Product entity);
}
