package org.uhanov.conroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.*;
import org.uhanov.dto.JwtAuthenticationResponse;
import org.uhanov.dto.SignInDto;
import org.uhanov.dto.user.MoneyTransferDto;
import org.uhanov.dto.user.UserFullDto;
import org.uhanov.dto.user.UserSignUpDto;
import org.uhanov.security.JwtAuthenticationFilter;
import org.uhanov.service.api.UserService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final ObjectMapper objectMapper;


    @PostMapping
    public ResponseEntity<UserFullDto> create(
            @RequestBody UserSignUpDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> signIn(
            @RequestBody SignInDto dto
    ) {
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(service.create(dto));
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.signIn(dto));
    }

//        @PreAuthorize("hasRole('USER')")
    @PreAuthorize("hasAnyAuthority('USER')")
    @PatchMapping("/{userId}")
    public ResponseEntity<UserFullDto> update(
            @RequestHeader Map<String, String> headers,
            @PathVariable("userId") UUID uuid,
            @RequestBody UserSignUpDto dto) {
        System.out.println("WWWWWWWWWWWWWWWW" + headers.get(JwtAuthenticationFilter.HEADER_NAME));
        dto.setId(uuid);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.update(dto));
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @DeleteMapping("/{userId}")
    public ResponseEntity delete(@PathVariable("userId") UUID uuid) {
        service.delete(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

//    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserFullDto> get(@PathVariable("userId") UUID uuid) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getById(uuid));
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PatchMapping("/{userId}/transfer")
    public ResponseEntity moneyTransfer(@PathVariable("userId") UUID senderId,
                                        @RequestBody MoneyTransferDto moneyTransferDto) {
        service.moneyTransfer(senderId, moneyTransferDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

}
