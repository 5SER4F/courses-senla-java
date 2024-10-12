package org.uhanov.conroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uhanov.dto.staff.StaffAuthDto;
import org.uhanov.dto.staff.StaffFullDto;
import org.uhanov.service.api.StaffService;

import java.util.UUID;

@RestController
@RequestMapping(path = "/staff")
@RequiredArgsConstructor
public class StaffController {
    private final StaffService service;
    private final ObjectMapper objectMapper;


    @PostMapping("")
    public ResponseEntity<StaffFullDto> create(
            @RequestBody StaffAuthDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    @PatchMapping("/{staffId}")
    public ResponseEntity update(
            @PathVariable("staffId") UUID staffId,
            @RequestBody StaffAuthDto dto
    ) {
        dto.setId(staffId);
        service.update(dto);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
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
