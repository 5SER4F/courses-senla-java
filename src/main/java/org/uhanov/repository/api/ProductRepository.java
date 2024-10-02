package org.uhanov.repository.api;

import org.uhanov.model.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends CrudRepository<Product> {
    Optional<Product> findByIdEager(UUID uuid);
}
