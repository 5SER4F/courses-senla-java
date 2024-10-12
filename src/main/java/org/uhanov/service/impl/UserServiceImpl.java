package org.uhanov.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.dto.mapper.UserMapper;
import org.uhanov.dto.user.MoneyTransferDto;
import org.uhanov.dto.user.UserAuthDto;
import org.uhanov.dto.user.UserFullDto;
import org.uhanov.exception.MoneyTransferException;
import org.uhanov.exception.ResourceNotFoundException;
import org.uhanov.model.User;
import org.uhanov.repository.api.UserRepository;
import org.uhanov.service.api.UserService;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserFullDto create(UserAuthDto dto) {
        return userMapper.toFullDto(
                userRepository.save(
                        userMapper.authToModel(dto)
                )
        );
    }

    @Transactional(readOnly = true)
    @Override
    public UserFullDto getById(UUID uuid) {
        return userMapper.toFullDto(get(uuid));
    }

    @Transactional
    @Override
    public void update(UserAuthDto dto) {
        User user = get(dto.getId());
        userMapper.updateUser(dto, user);
        userRepository.update(user);
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        userRepository.deleteById(uuid);
    }

    @Transactional
    @Override
    public void moneyTransfer(UUID senderId, MoneyTransferDto moneyTransferDto) {
        User sender = get(senderId);
        User recipient = get(moneyTransferDto.getRecipientId());
        sender.changeBalance(-moneyTransferDto.getAmount());
        recipient.changeBalance(moneyTransferDto.getAmount());
        userRepository.save(sender);
        userRepository.save(recipient);
        if (sender.getBalance() < 0) {
            throw new MoneyTransferException("User with id=" + senderId +
                    "trying transfer more money then have");
        }
    }

    private User get(UUID uuid) {
        Optional<User> user = userRepository.findById(uuid);
        return user.orElseThrow(
                ResourceNotFoundException::new
        );
    }
}
