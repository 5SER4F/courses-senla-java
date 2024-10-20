package org.uhanov.service.api;

import org.uhanov.dto.JwtAuthenticationResponse;
import org.uhanov.dto.SignInDto;
import org.uhanov.dto.user.MoneyTransferDto;
import org.uhanov.dto.user.UserSignUpDto;
import org.uhanov.dto.user.UserFullDto;

import java.util.UUID;


public interface UserService {

    UserFullDto create(UserSignUpDto dto);

    JwtAuthenticationResponse signIn(SignInDto signInDto);

    UserFullDto getById(UUID uuid);

    UserFullDto update(UserSignUpDto dto);

    void delete(UUID uuid);

    void moneyTransfer(UUID senderId, MoneyTransferDto moneyTransferDto);
}
