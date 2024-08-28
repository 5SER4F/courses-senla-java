package org.uhanov.dto.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.uhanov.dto.ProductDTO;
import org.uhanov.model.Product;

@Mapper(componentModel = "spring")
@Component
public interface ProductMapper {
    Product toModel(ProductDTO dto);

    ProductDTO toDto(Product product);
}
