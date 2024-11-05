package org.uhanov.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.uhanov.model.purchase.Purchase;
import org.uhanov.model.purchase.PurchaseStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {
    List<Purchase> findAllByBuyer_Id(UUID uuid);

    boolean existsByBuyer_IdAndProduct_IdAndPurchaseStatus(UUID customerId, UUID productId, PurchaseStatus purchaseStatus);

    @Query(
            "SELECT SUM(p.finalCost) FROM Purchase p WHERE p.product.id IN (:productsIds)" +
                    " AND p.purchaseDate >= :dateTime"
    )
    Double countFinalCostWhereProduct_IdInAndPurchaseDateAfter(
            @Param("productsIds") Iterable<UUID> productsIds, @Param("dateTime") LocalDateTime dateTime
    );

}
