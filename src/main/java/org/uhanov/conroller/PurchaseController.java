package org.uhanov.conroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uhanov.dto.purchase.PurchaseDto;
import org.uhanov.dto.purchase.PurchasePostDto;
import org.uhanov.service.api.PurchaseService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(path = "/purchases")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService service;

    @PostMapping("")
    public ResponseEntity<PurchaseDto> create(
            @RequestBody @Valid PurchasePostDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @DeleteMapping("/{purchaseId}")
    public ResponseEntity delete(
            @PathVariable("purchaseId") UUID purchaseId
    ) {
        service.delete(purchaseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }


    @GetMapping("/{purchaseId}")
    public ResponseEntity<PurchaseDto> get(
            @PathVariable("purchaseId") UUID purchaseId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getById(purchaseId));
    }

}
