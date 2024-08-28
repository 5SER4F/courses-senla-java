package org.uhanov.service.api;

import org.uhanov.dto.ProductDTO;

import java.util.UUID;

public interface ProductService {


    ProductDTO create(ProductDTO dto);


    ProductDTO getById(UUID uuid);

    void update(ProductDTO dto);


    boolean delete(UUID uuid);
}
