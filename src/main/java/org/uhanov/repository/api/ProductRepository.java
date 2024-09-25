package org.uhanov.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uhanov.model.Product;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
