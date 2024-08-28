package org.uhanov.service.api;

import org.uhanov.dto.PurchaseDTO;

import java.util.UUID;

public interface PurchaseService {


    PurchaseDTO create(PurchaseDTO dto);


    PurchaseDTO getById(UUID uuid);


    void update(PurchaseDTO dto);


    boolean delete(UUID uuid);
}
