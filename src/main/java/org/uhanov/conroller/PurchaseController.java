package org.uhanov.conroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.uhanov.dto.purchase.PurchaseDto;
import org.uhanov.dto.purchase.PurchasePostDto;
import org.uhanov.exception.InvalidLoginException;
import org.uhanov.security.JwtAuthenticationFilter;
import org.uhanov.security.JwtUtil;
import org.uhanov.service.api.PurchaseService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(path = "/purchases")
@RequiredArgsConstructor
@Slf4j
public class PurchaseController {
    private final PurchaseService service;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @PostMapping
    public ResponseEntity<PurchaseDto> create(
            @RequestBody @Valid PurchasePostDto dto,
            @RequestHeader(name = JwtAuthenticationFilter.HEADER_NAME)
            String jwtToken
    ) {
        log.info("Запрос на покупку:" + dto);
        compareIds(dto.getBuyerId(), jwtToken);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @DeleteMapping("/{purchaseId}")
    public ResponseEntity delete(
            @PathVariable("purchaseId") UUID purchaseId,
            @RequestHeader(name = JwtAuthenticationFilter.HEADER_NAME)
            String jwtToken
    ) {
        log.info("Запрос на возврат покупки id=" + purchaseId);
        service.delete(purchaseId, jwtUtil.extractId(jwtToken));
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }


    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'STAFF')")
    @GetMapping("/{purchaseId}")
    public ResponseEntity<PurchaseDto> get(
            @PathVariable("purchaseId") UUID purchaseId,
            @RequestHeader(name = JwtAuthenticationFilter.HEADER_NAME)
            String jwtToken
    ) {
        log.info("Запрос  покупки id=" + purchaseId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getById(
                        purchaseId,
                        jwtUtil.extractId(jwtToken),
                        jwtUtil.extractRole(jwtToken)
                ));
    }

    private void compareIds(UUID creatorId, String jwtToken) {
        if (!creatorId.equals(jwtUtil.extractId(jwtToken))) {
            throw new InvalidLoginException("Id польхователя и покупки должны совпадать");
        }
    }

}
