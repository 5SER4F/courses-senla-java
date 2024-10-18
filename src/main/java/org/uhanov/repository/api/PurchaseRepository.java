package org.uhanov.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uhanov.model.AgeRating;
import org.uhanov.model.Purchase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, UUID>
// CrudRepository<Purchase>
{
//    List<Purchase> findByPurchaseDateInPeriod(LocalDateTime left, LocalDateTime right);

    List<Purchase> findByProductName(String name);

//    Optional<Purchase> findByIdEager(UUID uuid);
}
