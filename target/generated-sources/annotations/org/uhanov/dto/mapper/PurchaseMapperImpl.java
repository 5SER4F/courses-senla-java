package org.uhanov.dto.mapper;

import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.uhanov.dto.PurchaseDTO;
import org.uhanov.model.Purchase;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-25T10:59:21+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20 (Oracle Corporation)"
)
@Component
public class PurchaseMapperImpl implements PurchaseMapper {

    @Override
    public Purchase toModel(PurchaseDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Purchase.PurchaseBuilder purchase = Purchase.builder();

        purchase.id( dto.getId() );
        purchase.userId( dto.getUserId() );
        purchase.productId( dto.getProductId() );
        if ( dto.getCost() != null ) {
            purchase.cost( BigDecimal.valueOf( dto.getCost() ) );
        }
        purchase.purchaseDate( dto.getPurchaseDate() );

        return purchase.build();
    }

    @Override
    public PurchaseDTO toDto(Purchase purchase) {
        if ( purchase == null ) {
            return null;
        }

        PurchaseDTO.PurchaseDTOBuilder purchaseDTO = PurchaseDTO.builder();

        purchaseDTO.id( purchase.getId() );
        purchaseDTO.userId( purchase.getUserId() );
        purchaseDTO.productId( purchase.getProductId() );
        if ( purchase.getCost() != null ) {
            purchaseDTO.cost( purchase.getCost().doubleValue() );
        }
        purchaseDTO.purchaseDate( purchase.getPurchaseDate() );

        return purchaseDTO.build();
    }

    @Override
    public void updatePurchase(PurchaseDTO dto, Purchase entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getUserId() != null ) {
            entity.setUserId( dto.getUserId() );
        }
        if ( dto.getProductId() != null ) {
            entity.setProductId( dto.getProductId() );
        }
        if ( dto.getCost() != null ) {
            entity.setCost( BigDecimal.valueOf( dto.getCost() ) );
        }
        if ( dto.getPurchaseDate() != null ) {
            entity.setPurchaseDate( dto.getPurchaseDate() );
        }
    }
}
