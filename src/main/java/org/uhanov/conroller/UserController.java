package org.uhanov.conroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uhanov.dto.user.MoneyTransferDto;
import org.uhanov.dto.user.UserAuthDto;
import org.uhanov.dto.user.UserFullDto;
import org.uhanov.service.api.UserService;

import java.util.UUID;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final ObjectMapper objectMapper;


    @PostMapping()
    public ResponseEntity<UserFullDto> create(
            @RequestBody UserAuthDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    @PatchMapping("/{userId}")
    public ResponseEntity<UserFullDto> update(
            @PathVariable("userId") UUID uuid,
            @RequestBody UserAuthDto dto) {
        dto.setId(uuid);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.update(dto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity delete(@PathVariable("userId") UUID uuid) {
        service.delete(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserFullDto> get(@PathVariable("userId") UUID uuid) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getById(uuid));
    }

    @PatchMapping("/{userId}/transfer")
    public ResponseEntity moneyTransfer(@PathVariable("userId") UUID senderId,
                                        @RequestBody MoneyTransferDto moneyTransferDto) {
        service.moneyTransfer(senderId, moneyTransferDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

}
