package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uhanov.dto.UserAuthDTO;
import org.uhanov.dto.UserFullDTO;
import org.uhanov.dto.mapper.UserMapper;
import org.uhanov.exception.EntityNotFoundException;
import org.uhanov.exception.MoneyTransferException;
import org.uhanov.model.User;
import org.uhanov.model.patcher.UserPatcher;
import org.uhanov.repository.api.UserRepository;
import org.uhanov.service.api.UserService;
import org.uhanov.transaction.Transaction;

import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserPatcher patcher;

    @Override
    public UserFullDTO create(UserAuthDTO dto) {
        return userMapper.toFullDto(
                userRepository.add(
                        userMapper.authToModel(dto)
                )
        );
    }

    @Override
    public UserFullDTO getById(UUID uuid) {
        return userMapper.toFullDto(get(uuid));
    }

    @Override
    public void update(UserAuthDTO dto) {
        User user = get(dto.getId());
        patcher.patchEntity(user, dto);
        userRepository.update(user);
    }

    @Override
    public boolean delete(UUID uuid) {
        userRepository.remove(uuid);
        return true;
    }

    @Transaction
    @Override
    public void moneyTransfer(UUID senderId, UUID recipientId, double amount) {
        User sender = get(senderId);
        User recipient = get(recipientId);
        sender.changeBalance(-amount);
        recipient.changeBalance(amount);
        userRepository.update(sender);
        userRepository.update(recipient);
        if (sender.getBalance() < 0) {
            throw new MoneyTransferException("User with id=" + senderId +
                    "trying transfer more money then have");
        }
    }

    private User get(UUID uuid) {
        return userRepository.get(uuid).orElseThrow(
                EntityNotFoundException::new
        );
    }
}
