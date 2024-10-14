package org.uhanov.service.api;

import org.uhanov.dto.purchase.PurchaseDto;
import org.uhanov.dto.purchase.PurchasePostDto;

import java.util.UUID;

public interface PurchaseService {
    PurchaseDto create(PurchasePostDto dto);

    PurchaseDto getById(UUID uuid);

    void delete(UUID uuid);
}
