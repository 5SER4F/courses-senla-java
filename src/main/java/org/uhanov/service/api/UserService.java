package org.uhanov.service.api;

import org.uhanov.dto.user.MoneyTransferDto;
import org.uhanov.dto.user.UserAuthDto;
import org.uhanov.dto.user.UserFullDto;

import java.util.UUID;


public interface UserService {

    UserFullDto create(UserAuthDto dto);

    UserFullDto getById(UUID uuid);

    UserFullDto update(UserAuthDto dto);

    void delete(UUID uuid);

    void moneyTransfer(UUID senderId, MoneyTransferDto moneyTransferDto);
}
