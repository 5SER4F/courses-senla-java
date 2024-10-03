package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.dto.ProductDTO;
import org.uhanov.dto.mapper.ProductMapper;
import org.uhanov.exception.EntityNotFoundException;
import org.uhanov.model.Product;
import org.uhanov.repository.api.ProductRepository;
import org.uhanov.service.api.ProductService;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@Data
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final ProductMapper productMapper;

    @Override
    public ProductDTO create(ProductDTO dto) {
        return productMapper.toDto(
                repository.save(
                        productMapper.toModel(dto)
                )
        );
    }

    @Transactional(readOnly = true)
    @Override
    public ProductDTO getById(UUID uuid) {
        return productMapper.toDto(getEntityById(uuid));
    }

    @Override
    public void update(ProductDTO dto) {
        Product product = getEntityById(dto.getId());
        productMapper.updateProduct(dto, product);
        repository.save(product);
    }

    @Override
    public boolean delete(UUID uuid) {
        repository.deleteById(uuid);
        return true;
    }

    private Product getEntityById(UUID uuid) {
        Optional<Product> product = repository.findById(uuid);
        return product
                .orElseThrow(EntityNotFoundException::new);
    }
}
