package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uhanov.dto.PurchaseDTO;
import org.uhanov.dto.mapper.PurchaseMapper;
import org.uhanov.exception.EntityNotFoundException;
import org.uhanov.model.Purchase;
import org.uhanov.model.patcher.PurchasePatcher;
import org.uhanov.repository.PurchaseRepositoryMock;
import org.uhanov.service.api.PurchaseService;

import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepositoryMock repository;
    private final PurchaseMapper purchaseMapper;
    private final PurchasePatcher patcher;

    @Override
    public PurchaseDTO create(PurchaseDTO dto) {
        return purchaseMapper.toDto(
                repository.saveEntity(
                        purchaseMapper.toModel(dto)
                )
        );
    }

    @Override
    public PurchaseDTO getById(UUID uuid) {
        return purchaseMapper.toDto(getEntityById(uuid));
    }

    @Override
    public void update(PurchaseDTO dto) {
        Purchase purchase = getEntityById(dto.getId());
        patcher.patchEntity(purchase, dto);
        repository.saveEntity(purchase);
    }

    @Override
    public boolean delete(UUID uuid) {
        return repository.removeByUUID(uuid);
    }

    private Purchase getEntityById(UUID uuid) {
        return repository.getByUUID(uuid)
                .orElseThrow(EntityNotFoundException::new);
    }
}
