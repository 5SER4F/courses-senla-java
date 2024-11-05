package org.uhanov.repository.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uhanov.model.product.Product;
import org.uhanov.model.purchase.PurchaseStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findAllByGenres_IdIn(Iterable<UUID> genresIds);


    List<Product> findAllByCreator_IdIn(Iterable<UUID> creatorsId);

    Page<Product> findAllByIdInAndDateAddedBetweenAndPriceLessThanOrderByPriceAsc(
            Iterable<UUID> productsIds, LocalDate minDate, LocalDate maxDate, BigDecimal maxPrice, Pageable pageable
    );

    Page<Product> findAllByDateAddedBetweenAndPriceLessThanOrderByPriceAsc(
            LocalDate minDate, LocalDate maxDate, BigDecimal maxPrice, Pageable pageable
    );

    List<Product> findAllByPurchaseOfProduct_Buyer_IdAndPurchaseOfProduct_PurchaseStatus(
            UUID customerId,
            PurchaseStatus purchaseStatus);

}
