package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.dto.PurchaseDTO;
import org.uhanov.dto.mapper.PurchaseMapper;
import org.uhanov.exception.EntityNotFoundException;
import org.uhanov.model.Purchase;
import org.uhanov.model.patcher.PurchasePatcher;
import org.uhanov.repository.api.PurchaseRepository;
import org.uhanov.repository.inMemory.PurchaseRepositoryMock;
import org.uhanov.service.api.PurchaseService;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@Data
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepository repository;
    private final PurchaseMapper purchaseMapper;

    @Override
    public PurchaseDTO create(PurchaseDTO dto) {
        return purchaseMapper.toDto(
                repository.save(
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
        purchaseMapper.updatePurchase(dto, purchase);
//        repository.saveEntity(purchase);
    }

    @Override
    public boolean delete(UUID uuid) {
        repository.deleteById(uuid);
        return true;
    }

    private Purchase getEntityById(UUID uuid) {
        Optional<Purchase> purchase = repository.findById(uuid);
        return purchase
                .orElseThrow(EntityNotFoundException::new);
    }
}
