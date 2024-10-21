package org.uhanov.conroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.uhanov.dto.JwtAuthenticationResponse;
import org.uhanov.dto.SignInDto;
import org.uhanov.dto.staff.StaffFullDto;
import org.uhanov.dto.staff.StaffSignUpDto;
import org.uhanov.service.api.StaffService;

import java.util.UUID;

@RestController
@RequestMapping(path = "/staff")
@RequiredArgsConstructor
public class StaffController {
    private final StaffService service;


    @PostMapping
    public ResponseEntity<StaffFullDto> create(
            @RequestBody StaffSignUpDto dto
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

    @PreAuthorize("hasAnyAuthority('STAFF')")
    @PatchMapping("/{staffId}")
    public ResponseEntity<StaffFullDto> update(
            @PathVariable("staffId") UUID staffId,
            @RequestBody StaffSignUpDto dto
    ) {
        dto.setId(staffId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.update(dto));
    }

    @PreAuthorize("hasAnyAuthority('STAFF')")
    @DeleteMapping("/{staffId}")
    public ResponseEntity delete(
            @PathVariable("staffId") UUID staffId
    ) {
        service.delete(staffId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{staffId}")
    public ResponseEntity<StaffFullDto> get(
            @PathVariable("staffId") UUID staffId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getById(staffId));
    }

}
