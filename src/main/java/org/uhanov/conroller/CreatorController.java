package org.uhanov.conroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uhanov.dto.creator.CreatorAuthDto;
import org.uhanov.dto.creator.CreatorDto;
import org.uhanov.service.api.CreatorService;

import java.util.UUID;

@RestController
@RequestMapping("/creators")
@RequiredArgsConstructor
public class CreatorController {
    private final CreatorService service;

    @PostMapping("")
    public ResponseEntity<CreatorDto> create(
            @RequestBody CreatorAuthDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    @PatchMapping("/{creatorId}")
    public ResponseEntity update(
            @PathVariable("creatorId") UUID creatorId,
            @RequestBody CreatorAuthDto dto
    ) {
        dto.setId(creatorId);
        service.update(dto);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/{creatorId}")
    public ResponseEntity delete(
            @PathVariable("creatorId") UUID creatorId
    ) {
        service.delete(creatorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{creatorId}")
    public ResponseEntity<CreatorDto> get(
            @PathVariable("creatorId") UUID creatorId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getById(creatorId));
    }

}
