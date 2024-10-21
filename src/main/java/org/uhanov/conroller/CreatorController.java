package org.uhanov.conroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uhanov.dto.JwtAuthenticationResponse;
import org.uhanov.dto.SignInDto;
import org.uhanov.dto.creator.CreatorDto;
import org.uhanov.dto.creator.CreatorSignUpDto;
import org.uhanov.service.api.CreatorService;

import java.util.UUID;

@RestController
@RequestMapping("/creators")
@RequiredArgsConstructor
public class CreatorController {
    private final CreatorService service;

    @PostMapping
    public ResponseEntity<CreatorDto> create(
            @RequestBody CreatorSignUpDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> signIn(
            @RequestBody SignInDto dto
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.signIn(dto));
    }

    @PatchMapping("/{creatorId}")
    public ResponseEntity<CreatorDto> update(
            @PathVariable("creatorId") UUID creatorId,
            @RequestBody CreatorSignUpDto dto
    ) {
        dto.setId(creatorId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.update(dto));
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
