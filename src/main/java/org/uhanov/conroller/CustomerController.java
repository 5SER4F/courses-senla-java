package org.uhanov.conroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.uhanov.dto.product.ProductDto;
import org.uhanov.dto.purchase.PurchaseDto;
import org.uhanov.dto.user.CustomerFullDto;
import org.uhanov.dto.user.CustomerPostDto;
import org.uhanov.dto.user.CustomerShortDto;
import org.uhanov.dto.user.MoneyTransferDto;
import org.uhanov.exception.InvalidLoginException;
import org.uhanov.security.JwtAuthenticationFilter;
import org.uhanov.security.JwtUtil;
import org.uhanov.service.api.CustomerService;
import org.uhanov.service.api.ProductService;
import org.uhanov.service.api.PurchaseService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/customer")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    private final CustomerService service;
    private final ProductService productService;
    private final PurchaseService purchaseService;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @PatchMapping("/{customerId}")
    public ResponseEntity<CustomerFullDto> update(
            @PathVariable("customerId") UUID uuid,
            @RequestBody CustomerPostDto dto,
            @RequestHeader(name = JwtAuthenticationFilter.HEADER_NAME)
            String jwtToken
    ) {
        dto.setId(uuid);
        log.info("Обновление покупателя:" + dto);
        compareIds(uuid, jwtToken);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.update(dto));
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerFullDto> get(
            @PathVariable("customerId") UUID customerId,
            @RequestHeader(name = JwtAuthenticationFilter.HEADER_NAME)
            String jwtToken
    ) {
        log.info("Запрос покупателя id=" + customerId);
        compareIds(customerId, jwtToken);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getById(customerId));
    }

    @GetMapping
    public ResponseEntity<Page<CustomerShortDto>> getAll(
            @RequestParam(name = "nickname", required = false, defaultValue = "")
            String nickname,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @PositiveOrZero
            int page,
            @RequestParam(name = "size", required = false, defaultValue = "10")
            @Positive
            int size
    ) {
        log.info(String.format(
                "Запрос всех покупателей с пагинацией nickname=%s, page=%d, size=%d", nickname, page, size
        ));
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAll(nickname, PageRequest.of(page, size)));
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @PatchMapping("/{customerId}/transfer")
    public ResponseEntity moneyTransfer(
            @PathVariable("customerId") UUID senderId,
            @RequestBody MoneyTransferDto moneyTransferDto,
            @RequestHeader(name = JwtAuthenticationFilter.HEADER_NAME)
            String jwtToken
    ) {
        log.info(String.format(
                "Запрос на перевод средства senderId=%s, recipientId=%s, amount=%f", senderId,
                moneyTransferDto.getRecipientId(), moneyTransferDto.getAmount()
        ));
        compareIds(senderId, jwtToken);
        service.moneyTransfer(senderId, moneyTransferDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @GetMapping("/{customerId}/library")
    public ResponseEntity<List<ProductDto>> getLibrary(
            @PathVariable("customerId") UUID customerId,
            @RequestHeader(name = JwtAuthenticationFilter.HEADER_NAME)
            String jwtToken
    ) {
        log.info("Запрос библиотеки пользователя с id=" + customerId);
        compareIds(customerId, jwtToken);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.loadCustomerLibrary(customerId));
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @GetMapping("/{customerId}/purchase")
    public ResponseEntity<List<PurchaseDto>> getPurchase(
            @PathVariable("customerId") UUID customerId,
            @RequestHeader(name = JwtAuthenticationFilter.HEADER_NAME)
            String jwtToken
    ) {
        log.info("Запрос покупок пользователя с id=" + customerId);
        compareIds(customerId, jwtToken);
        return ResponseEntity.status(HttpStatus.OK)
                .body(purchaseService.getCustomerPurchase(customerId));
    }

    private void compareIds(UUID creatorId, String jwtToken) {
        if (!creatorId.equals(jwtUtil.extractId(jwtToken))) {
            throw new InvalidLoginException("Попытка попытка доступа к чужому аккаунту");
        }
    }

}
