package org.uhanov.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.uhanov.dto.product.ProductDto;
import org.uhanov.dto.product.ProductGetAllParams;
import org.uhanov.dto.product.ProductPostDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {


    ProductDto create(ProductPostDto dto, UUID idFromToken);


    ProductDto getById(UUID uuid);

    ProductDto update(ProductPostDto dto, UUID idFromToken);

    void delete(UUID uuid, UUID idFromToken);

    Page<ProductDto> findAllWithParam(
            ProductGetAllParams params, Double maxPrice,
            Pageable pageable

    );

    List<ProductDto> loadCustomerLibrary(UUID customerId);
}
