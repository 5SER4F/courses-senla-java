package org.uhanov.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.dto.JwtAuthenticationResponse;
import org.uhanov.dto.SignInDto;
import org.uhanov.dto.mapper.UserMapper;
import org.uhanov.dto.user.MoneyTransferDto;
import org.uhanov.dto.user.UserSignUpDto;
import org.uhanov.dto.user.UserFullDto;
import org.uhanov.exception.MoneyTransferException;
import org.uhanov.exception.ResourceNotFoundException;
import org.uhanov.model.User;
import org.uhanov.repository.api.UserRepository;
import org.uhanov.security.JwtUtil;
import org.uhanov.service.api.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final JwtUtil jwtUtils;

    private final AuthenticationManager authenticationManager;

    @Transactional
    @Override
    public UserFullDto create(UserSignUpDto dto) {
        return userMapper.toFullDto(
                userRepository.save(
                        userMapper.authToModel(dto)
                )
        );
    }

    @Transactional
    @Override
    public JwtAuthenticationResponse signIn(SignInDto signInDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInDto.getUsername(),
                        signInDto.getPassword(),
                        List.of(new SimpleGrantedAuthority(signInDto.getRole()))
                )
        );

        UserDetails user = userRepository.findUserDetailsByNickname(signInDto.getUsername());

        String jwt = jwtUtils.generateToken(user);

        return new JwtAuthenticationResponse(jwt);
    }

    @Transactional(readOnly = true)
    @Override
    public UserFullDto getById(UUID uuid) {
        return userMapper.toFullDto(get(uuid));
    }

    @Transactional
    @Override
    public UserFullDto update(UserSignUpDto dto) {
        User user = get(dto.getId());
        userMapper.updateUser(dto, user);
        return userMapper.toFullDto(
                userRepository.save(user)
        );
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
