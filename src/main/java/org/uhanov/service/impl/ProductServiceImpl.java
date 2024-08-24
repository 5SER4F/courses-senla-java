package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uhanov.dto.ProductDTO;
import org.uhanov.dto.mapper.ProductMapper;
import org.uhanov.exception.EntityNotFoundException;
import org.uhanov.exception.PatchWithoutIdException;
import org.uhanov.model.Product;
import org.uhanov.model.patcher.ProductPatcher;
import org.uhanov.repository.ProductRepositoryMock;
import org.uhanov.service.api.ProductService;

import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepositoryMock repository;
    private final ProductMapper productMapper;
    private final ProductPatcher patcher;

    @Override
    public ProductDTO create(ProductDTO dto) {
        return productMapper.toDto(
                repository.addEntity(
                        productMapper.toModel(dto)
                )
        );
    }

    @Override
    public ProductDTO getById(UUID uuid) {
        return productMapper.toDto(getEntityById(uuid));
    }

    @Override
    public ProductDTO update(ProductDTO dto) {
        Product oldProduct = getEntityById(dto.getId());
        Product patch = productMapper.toModel(dto);
        if (patch.getId() == null) {
            throw new PatchWithoutIdException();
        }
        Product patchedProduct = patcher.patchEntity(oldProduct, patch);
        repository.addEntity(patchedProduct);
        return productMapper.toDto(patchedProduct);
    }

    @Override
    public boolean delete(UUID uuid) {
        return repository.removeByUUID(uuid);
    }

    private Product getEntityById(UUID uuid) {
        return repository.getByUUID(uuid)
                .orElseThrow(EntityNotFoundException::new);
    }
}
