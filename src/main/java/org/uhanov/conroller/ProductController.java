package org.uhanov.conroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uhanov.dto.product.ProductDto;
import org.uhanov.dto.product.ProductPostDto;
import org.uhanov.service.api.ProductService;

import java.util.UUID;

@RestController
@RequestMapping(path = "/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping
    public ResponseEntity<ProductDto> create(
            @RequestBody ProductPostDto dto
    ) {
        ProductDto response = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }


    @PatchMapping("/{productId}")
    public ResponseEntity<ProductDto> update(
            @PathVariable("productId") UUID productId,
            @RequestBody ProductPostDto dto
    ) {
        dto.setId(productId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.update(dto));
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity delete(
            @PathVariable("productId") UUID productId
    ) {
        service.delete(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> get(
            @PathVariable("productId") UUID productId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getById(productId));
    }

}
