package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.dto.JwtAuthenticationResponse;
import org.uhanov.dto.SignInDto;
import org.uhanov.dto.creator.CreatorDto;
import org.uhanov.dto.creator.CreatorPostDto;
import org.uhanov.dto.staff.StaffFullDto;
import org.uhanov.dto.staff.StaffPostDto;
import org.uhanov.dto.user.CustomerFullDto;
import org.uhanov.dto.user.CustomerPostDto;
import org.uhanov.exception.InvalidLoginException;
import org.uhanov.exception.ResourceNotFoundException;
import org.uhanov.exception.SignUpException;
import org.uhanov.model.user.AccountStatus;
import org.uhanov.model.user.Role;
import org.uhanov.model.user.User;
import org.uhanov.repository.api.UserRepository;
import org.uhanov.security.JwtUtil;
import org.uhanov.service.api.CreatorService;
import org.uhanov.service.api.CustomerService;
import org.uhanov.service.api.StaffService;
import org.uhanov.service.api.UserService;

import java.util.UUID;

@Transactional
@Service
@Data
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CustomerService customerService;
    private final CreatorService creatorService;
    private final StaffService staffService;
    private final JwtUtil jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    @Transactional
    @Override
    public JwtAuthenticationResponse signIn(SignInDto signInDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInDto.getUsername(),
                        signInDto.getPassword()
                )
        );
        UserDetails userDetails = userRepository.findUserDetailsByUsername(signInDto.getUsername());
        if (!userDetails.isEnabled()) {
            throw new InvalidLoginException("Попытка войти в удаленный аккаунт");
        }
        String jwt = jwtUtils.generateToken(userDetails);

        return new JwtAuthenticationResponse(jwt);
    }

    @Transactional
    @Override
    public CustomerFullDto signUpCustomer(CustomerPostDto customerPostDto) {
        checkDuplicate(
                customerPostDto.getUsername(),
                customerPostDto.getEmail()
        );

        String encodedPassword = encoder.encode(customerPostDto.getPassword());
        customerPostDto.setPassword(encodedPassword);

        customerPostDto.setRole(Role.CUSTOMER);
        return customerService.create(customerPostDto);
    }

    @Transactional
    @Override
    public CreatorDto signUpCreator(CreatorPostDto creatorPostDto) {
        checkDuplicate(
                creatorPostDto.getUsername(),
                creatorPostDto.getEmail()
        );

        String encodedPassword = encoder.encode(creatorPostDto.getPassword());
        creatorPostDto.setPassword(encodedPassword);

        creatorPostDto.setRole(Role.CREATOR);
        return creatorService.create(creatorPostDto);
    }

    @Transactional
    @Override
    public StaffFullDto signUpStaff(StaffPostDto staffPostDto) {
        checkDuplicate(
                staffPostDto.getUsername(),
                staffPostDto.getEmail()
        );

        String encodedPassword = encoder.encode(staffPostDto.getPassword());
        staffPostDto.setPassword(encodedPassword);

        staffPostDto.setRole(Role.STAFF);
        return staffService.create(staffPostDto);
    }

    @Transactional
    @Override
    public void deleteAccount(UUID id) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        userToDelete.setAccountStatus(AccountStatus.DELETED);
        userRepository.save(userToDelete);
    }

    private void checkDuplicate(String username, String email) {
        if (
                userRepository.existsByUsernameOrEmail(username, email)
        ) {
            throw new SignUpException(
                    String.format(
                            "Пользователь с таким username=%s или email=%s уже существует", username, email
                    )
            );
        }
    }
}
