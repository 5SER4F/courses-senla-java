package org.uhanov.conroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.uhanov.dto.staff.StaffFullDto;
import org.uhanov.dto.staff.StaffPostDto;
import org.uhanov.exception.InvalidLoginException;
import org.uhanov.security.JwtAuthenticationFilter;
import org.uhanov.security.JwtUtil;
import org.uhanov.service.api.StaffService;

import java.util.UUID;

@RestController
@RequestMapping(path = "/staff")
@RequiredArgsConstructor
@Slf4j
public class StaffController {
    private final StaffService service;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasAnyAuthority('STAFF')")
    @PatchMapping("/{staffId}")
    public ResponseEntity<StaffFullDto> update(
            @PathVariable("staffId") UUID staffId,
            @RequestBody StaffPostDto dto,
            @RequestHeader(name = JwtAuthenticationFilter.HEADER_NAME)
            String jwtToken
    ) {
        compareIds(staffId, jwtToken);
        dto.setId(staffId);
        log.info("Обновление сотрудника:" + dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.update(dto));
    }

    @PreAuthorize("hasAnyAuthority('STAFF')")
    @GetMapping("/{staffId}")
    public ResponseEntity<StaffFullDto> get(
            @PathVariable("staffId") UUID staffId,
            @RequestHeader(name = JwtAuthenticationFilter.HEADER_NAME)
            String jwtToken
    ) {
        log.info("Запрос сотрудника:" + staffId);
        compareIds(staffId, jwtToken);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getById(staffId));
    }

    private void compareIds(UUID creatorId, String jwtToken) {
        if (!creatorId.equals(jwtUtil.extractId(jwtToken))) {
            throw new InvalidLoginException("Попытка попытка доступа к чужому аккаунту");
        }
    }

}
