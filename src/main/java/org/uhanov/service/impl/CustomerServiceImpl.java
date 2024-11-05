package org.uhanov.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.dto.mapper.CustomerMapper;
import org.uhanov.dto.user.CustomerFullDto;
import org.uhanov.dto.user.CustomerPostDto;
import org.uhanov.dto.user.CustomerShortDto;
import org.uhanov.dto.user.MoneyTransferDto;
import org.uhanov.exception.MoneyTransferException;
import org.uhanov.exception.ResourceNotFoundException;
import org.uhanov.model.Customer;
import org.uhanov.model.user.AccountStatus;
import org.uhanov.repository.api.CustomerRepository;
import org.uhanov.security.JwtUtil;
import org.uhanov.service.api.CustomerService;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper customerMapper;

    @Transactional
    @Override
    public CustomerFullDto create(CustomerPostDto dto) {
        Customer newCustomer = customerMapper.authToModel(dto);
        newCustomer.setAccountStatus(AccountStatus.CREATED);
        return customerMapper.toFullDto(
                repository.save(
                        newCustomer
                )
        );
    }


    @Transactional(readOnly = true)
    @Override
    public CustomerFullDto getById(UUID uuid) {
        return customerMapper.toFullDto(get(uuid));
    }

    @Transactional
    @Override
    public CustomerFullDto update(CustomerPostDto dto) {
        Customer customer = get(dto.getId());
        customerMapper.updateUser(dto, customer);
        return customerMapper.toFullDto(
                repository.save(customer)
        );
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        repository.deleteById(uuid);
    }

    @Transactional
    @Override
    public void moneyTransfer(UUID senderId, MoneyTransferDto moneyTransferDto) {
        Customer sender = get(senderId);
        Customer recipient = get(moneyTransferDto.getRecipientId());
        sender.changeBalance(-moneyTransferDto.getAmount());
        recipient.changeBalance(moneyTransferDto.getAmount());
        repository.save(sender);
        repository.save(recipient);
        if (sender.getBalance() < 0) {
            throw new MoneyTransferException("Покупатель id=" + senderId +
                    "пытается перевести больше денег, чем имеет");
        }
    }

    @Transactional
    @Override
    public Page<CustomerShortDto> getAll(String nickname, Pageable pageable) {
        return repository.findAllByNicknameContainingIgnoreCase(nickname, pageable)
                .map(customerMapper::toShortDto);
    }

    private Customer get(UUID uuid) {
        Optional<Customer> user = repository.findById(uuid);
        return user.orElseThrow(
                ResourceNotFoundException::new
        );
    }
}
