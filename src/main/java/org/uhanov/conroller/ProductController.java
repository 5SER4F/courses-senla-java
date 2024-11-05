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
import org.uhanov.dto.product.ProductGetAllParams;
import org.uhanov.dto.product.ProductPostDto;
import org.uhanov.security.JwtAuthenticationFilter;
import org.uhanov.security.JwtUtil;
import org.uhanov.service.api.ProductService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.UUID;

@RestController
@RequestMapping(path = "/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService service;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasAnyAuthority('CREATOR')")
    @PostMapping
    public ResponseEntity<ProductDto> create(
            @RequestBody ProductPostDto dto,
            @RequestHeader(name = JwtAuthenticationFilter.HEADER_NAME)
            String jwtToken
    ) {
        log.info("Запрос на создание продукта:" + dto);
        ProductDto response = service.create(
                dto,
                jwtUtil.extractId(jwtToken)
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }


    @PreAuthorize("hasAnyAuthority('CREATOR','STAFF')")
    @PatchMapping("/{productId}")
    public ResponseEntity<ProductDto> update(
            @PathVariable("productId")
            UUID productId,
            @RequestBody
            ProductPostDto dto,
            @RequestHeader(name = JwtAuthenticationFilter.HEADER_NAME)
            String jwtToken
    ) {
        dto.setId(productId);
        log.info("Запрос на обновление продукта" + dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.update(
                                dto,
                                jwtUtil.extractId(jwtToken)
                        )
                );
    }


    @PreAuthorize("hasAnyAuthority('CREATOR')")
    @DeleteMapping("/{productId}")
    public ResponseEntity delete(
            @PathVariable("productId")
            UUID productId,
            @RequestHeader(name = JwtAuthenticationFilter.HEADER_NAME)
            String jwtToken
    ) {
        log.info("Запрос на удаление продукта id" + productId);
        service.delete(
                productId,
                jwtUtil.extractId(jwtToken)
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> get(
            @PathVariable("productId") UUID productId
    ) {
        log.info("Запрос продукта id" + productId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getById(productId));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ProductDto>> getAllWithParam(
            @RequestBody ProductGetAllParams productGetAllParams,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @PositiveOrZero
            int page,
            @RequestParam(name = "size", required = false, defaultValue = "10")
            @Positive
            int size,
            @RequestParam(name = "maxPrice", required = false, defaultValue = 1_000_000 + "")
            double maxPrice
    ) {
        log.info(String.format(
                "Запрос всех продуктов с пагинацией page=%d, size=%d, maxPrice=%f, params=%s",
                page, size, maxPrice, productGetAllParams
        ));
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        service.findAllWithParam(
                                productGetAllParams,
                                maxPrice,
                                PageRequest.of(page, size)
                        )
                );
    }

}
