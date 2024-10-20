package org.uhanov.conroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uhanov.dto.staff.StaffSignUpDto;
import org.uhanov.dto.staff.StaffFullDto;
import org.uhanov.service.api.StaffService;

import java.util.UUID;

@RestController
@RequestMapping(path = "/staff")
@RequiredArgsConstructor
public class StaffController {
    private final StaffService service;
    private final ObjectMapper objectMapper;


    @PostMapping
    public ResponseEntity<StaffFullDto> create(
            @RequestBody StaffSignUpDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    @PatchMapping("/{staffId}")
    public ResponseEntity<StaffFullDto> update(
            @PathVariable("staffId") UUID staffId,
            @RequestBody StaffSignUpDto dto
    ) {
        dto.setId(staffId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.update(dto));
    }


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
