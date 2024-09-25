package org.uhanov.dto.mapper;

import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.uhanov.dto.ProductDTO;
import org.uhanov.model.Product;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-25T10:59:21+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toModel(ProductDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.id( dto.getId() );
        product.creatorId( dto.getCreatorId() );
        product.name( dto.getName() );
        product.dateAdded( dto.getDateAdded() );
        product.ageRatingId( dto.getAgeRatingId() );
        if ( dto.getPrice() != null ) {
            product.price( BigDecimal.valueOf( dto.getPrice() ) );
        }

        return product.build();
    }

    @Override
    public ProductDTO toDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDTO.ProductDTOBuilder productDTO = ProductDTO.builder();

        productDTO.id( product.getId() );
        productDTO.creatorId( product.getCreatorId() );
        productDTO.name( product.getName() );
        productDTO.dateAdded( product.getDateAdded() );
        productDTO.ageRatingId( product.getAgeRatingId() );
        if ( product.getPrice() != null ) {
            productDTO.price( product.getPrice().doubleValue() );
        }

        return productDTO.build();
    }

    @Override
    public void updateProduct(ProductDTO dto, Product entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getCreatorId() != null ) {
            entity.setCreatorId( dto.getCreatorId() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getDateAdded() != null ) {
            entity.setDateAdded( dto.getDateAdded() );
        }
        if ( dto.getAgeRatingId() != null ) {
            entity.setAgeRatingId( dto.getAgeRatingId() );
        }
        if ( dto.getPrice() != null ) {
            entity.setPrice( BigDecimal.valueOf( dto.getPrice() ) );
        }
    }
}
