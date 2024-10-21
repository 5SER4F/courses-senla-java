package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.dto.JwtAuthenticationResponse;
import org.uhanov.dto.SignInDto;
import org.uhanov.dto.creator.CreatorDto;
import org.uhanov.dto.creator.CreatorSignUpDto;
import org.uhanov.dto.mapper.CreatorMapper;
import org.uhanov.exception.ResourceNotFoundException;
import org.uhanov.model.Creator;
import org.uhanov.repository.api.CreatorRepository;
import org.uhanov.security.JwtUtil;
import org.uhanov.service.api.CreatorService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@Data
@RequiredArgsConstructor
public class CreatorServiceImpl implements CreatorService {
    private final CreatorRepository repository;
    private final CreatorMapper creatorMapper;
    private final JwtUtil jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Transactional
    @Override
    public CreatorDto create(CreatorSignUpDto creatorSignUpDto) {
        return creatorMapper.toDto(
                repository.save(
                        creatorMapper.authToModel(creatorSignUpDto)
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

        UserDetails user = repository.findCreatorDetailsByName(signInDto.getUsername());

        String jwt = jwtUtils.generateToken(user);

        return new JwtAuthenticationResponse(jwt);
    }

    @Transactional(readOnly = true)
    @Override
    public CreatorDto getById(UUID uuid) {
        return creatorMapper.toDto(get(uuid));
    }

    @Transactional
    @Override
    public CreatorDto update(CreatorSignUpDto creatorSignUpDto) {
        Creator creator = get(creatorSignUpDto.getId());
        creatorMapper.updateCreator(creatorSignUpDto, creator);
        return creatorMapper.toDto(
                repository.save(creator)
        );
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        repository.deleteById(uuid);
    }

    private Creator get(UUID uuid) {
        Optional<Creator> creator = repository.findById(uuid);
        return creator
                .orElseThrow(ResourceNotFoundException::new);
    }
}
