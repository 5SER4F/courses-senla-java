package org.uhanov.conroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.uhanov.dto.JwtAuthenticationResponse;
import org.uhanov.dto.SignInDto;
import org.uhanov.dto.creator.CreatorDto;
import org.uhanov.dto.creator.CreatorPostDto;
import org.uhanov.dto.staff.StaffFullDto;
import org.uhanov.dto.staff.StaffPostDto;
import org.uhanov.dto.user.CustomerFullDto;
import org.uhanov.dto.user.CustomerPostDto;
import org.uhanov.exception.InvalidLoginException;
import org.uhanov.security.JwtAuthenticationFilter;
import org.uhanov.security.JwtUtil;
import org.uhanov.service.api.UserService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/account")
@Slf4j
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signIn")
    public ResponseEntity<JwtAuthenticationResponse> signIn(
            @RequestBody
            @Valid
            SignInDto signInDto
    ) {
        log.info(
                "Авторизация пользователя:" + signInDto
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.signIn(signInDto));
    }

    @PostMapping("/signUp/customer")
    public ResponseEntity<CustomerFullDto> signUpCustomer(
            @RequestBody
            @Valid
            CustomerPostDto customerPostDto
    ) {
        log.info(
                "Запрос на регистрацию покупателя:" + customerPostDto
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.signUpCustomer(customerPostDto));
    }

    @PostMapping("/signUp/creator")
    public ResponseEntity<CreatorDto> signUpCreator(
            @RequestBody
            @Valid CreatorPostDto creatorPostDto
    ) {
        log.info(
                "Запрос на регистрацию создателя:" + creatorPostDto
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.signUpCreator(creatorPostDto));
    }


    /**
     * Аккаунт новым сотрудникам может создавать только другой сотрудник
     */
    @PreAuthorize("hasAnyAuthority('STAFF')")
    @PostMapping("/signUp/staff")
    public ResponseEntity<StaffFullDto> sigUpStaff(
            @RequestBody
            @Valid
            StaffPostDto staffPostDto
    ) {
        log.info(
                "Запрос на регистрацию сотрудника:" + staffPostDto
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.signUpStaff(staffPostDto));
    }

    @PreAuthorize("hasAnyAuthority('STAFF', 'CREATOR', 'CUSTOMER')")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity delete(
            @PathVariable("userId") UUID id,
            @RequestHeader(name = JwtAuthenticationFilter.HEADER_NAME)
            String jwtToken
    ) {
        if (!id.equals(jwtUtil.extractId(jwtToken))) {
            throw new InvalidLoginException("Попытка попытка удалить чужой аккаунт");
        }
        log.info(
                "Запрос на удаление аккаунта с id=" + id
        );
        userService.deleteAccount(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
