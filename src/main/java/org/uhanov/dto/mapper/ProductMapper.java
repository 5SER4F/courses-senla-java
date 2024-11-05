package org.uhanov.dto.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import org.uhanov.dto.product.ProductDto;
import org.uhanov.dto.product.ProductPostDto;
import org.uhanov.model.product.Product;

@Mapper(componentModel = "spring", uses = {CreatorMapper.class, GenreMapper.class})
@Component
public interface ProductMapper {
    Product toModel(ProductPostDto dto);

    ProductDto toDto(Product product);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProduct(ProductPostDto dto, @MappingTarget Product entity);
}
