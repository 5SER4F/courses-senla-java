package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uhanov.dto.PurchaseDTO;
import org.uhanov.dto.mapper.PurchaseMapper;
import org.uhanov.exception.EntityNotFoundException;
import org.uhanov.exception.PatchWithoutIdException;
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
                repository.addEntity(
                        purchaseMapper.toModel(dto)
                )
        );
    }

    @Override
    public PurchaseDTO getById(UUID uuid) {
        return purchaseMapper.toDto(getEntityById(uuid));
    }

    @Override
    public PurchaseDTO update(PurchaseDTO dto) {
        Purchase oldPurchase = getEntityById(dto.getId());
        Purchase patch = purchaseMapper.toModel(dto);
        if (patch.getId() == null) {
            throw new PatchWithoutIdException();
        }
        Purchase patchedPurchase = patcher.patchEntity(oldPurchase, patch);
        repository.addEntity(patchedPurchase);
        return purchaseMapper.toDto(patchedPurchase);
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
