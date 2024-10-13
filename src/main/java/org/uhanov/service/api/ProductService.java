package org.uhanov.service.api;

import org.uhanov.dto.product.ProductDto;
import org.uhanov.dto.product.ProductPostDto;

import java.util.UUID;

public interface ProductService {


    ProductDto create(ProductPostDto dto);


    ProductDto getById(UUID uuid);

    ProductDto update(ProductPostDto dto);


    void delete(UUID uuid);
}
