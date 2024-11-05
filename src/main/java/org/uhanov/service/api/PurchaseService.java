package org.uhanov.service.api;

import org.uhanov.dto.purchase.PurchaseDto;
import org.uhanov.dto.purchase.PurchasePostDto;
import org.uhanov.model.user.Role;

import java.util.List;
import java.util.UUID;

public interface PurchaseService {
    PurchaseDto create(PurchasePostDto dto);

    PurchaseDto getById(UUID uuid, UUID customerId, Role role);

    void delete(UUID purchaseId, UUID customerId);

    List<PurchaseDto> getCustomerPurchase(UUID customerId);
}
