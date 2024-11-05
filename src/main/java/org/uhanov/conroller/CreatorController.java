package org.uhanov.conroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.uhanov.dto.creator.CreatorDto;
import org.uhanov.dto.creator.CreatorPostDto;
import org.uhanov.exception.InvalidLoginException;
import org.uhanov.security.JwtAuthenticationFilter;
import org.uhanov.security.JwtUtil;
import org.uhanov.service.api.CreatorService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/creators")
@RequiredArgsConstructor
@Slf4j
public class CreatorController {
    private final CreatorService service;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasAnyAuthority('CREATOR')")
    @PatchMapping("/{creatorId}")
    public ResponseEntity<CreatorDto> update(
            @PathVariable("creatorId")
            UUID creatorId,
            @RequestBody
            @Valid
            CreatorPostDto dto,
            @RequestHeader(name = JwtAuthenticationFilter.HEADER_NAME)
            String jwtToken
    ) {
        dto.setId(creatorId);
        log.info("Обновление создателя:" + dto);
        compareIds(creatorId, jwtToken);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.update(dto));
    }

    @GetMapping("/{creatorId}")
    public ResponseEntity<CreatorDto> get(
            @PathVariable("creatorId") UUID creatorId
    ) {
        log.info("Запрос создателя id=" + creatorId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getById(creatorId));
    }

    @PreAuthorize("hasAnyAuthority('CREATOR')")
    @PatchMapping("/{creatorId}/profit")
    public ResponseEntity countProfit(
            @PathVariable("creatorId")
            UUID creatorId,
            @RequestHeader(name = JwtAuthenticationFilter.HEADER_NAME)
            String jwtToken
    ) {
        log.info("Запрос на подсчет прибыли id=" + creatorId);
        compareIds(creatorId, jwtToken);
        service.countProfit(creatorId);
        return ResponseEntity
                .ok()
                .build();
    }

    private void compareIds(UUID creatorId, String jwtToken) {
        if (!creatorId.equals(jwtUtil.extractId(jwtToken))) {
            throw new InvalidLoginException("Попытка попытка доступа к чужому аккаунту");
        }
    }
}
