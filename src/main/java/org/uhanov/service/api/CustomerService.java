package org.uhanov.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.uhanov.dto.user.CustomerFullDto;
import org.uhanov.dto.user.CustomerPostDto;
import org.uhanov.dto.user.CustomerShortDto;
import org.uhanov.dto.user.MoneyTransferDto;

import java.util.UUID;


public interface CustomerService {

    CustomerFullDto create(CustomerPostDto dto);

    CustomerFullDto getById(UUID uuid);

    CustomerFullDto update(CustomerPostDto dto);

    void delete(UUID uuid);

    void moneyTransfer(UUID senderId, MoneyTransferDto moneyTransferDto);

    Page<CustomerShortDto> getAll(String nickname, Pageable pageable);
}
