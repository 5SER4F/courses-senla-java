package org.uhanov.service.api;

import org.uhanov.dto.JwtAuthenticationResponse;
import org.uhanov.dto.SignInDto;
import org.uhanov.dto.creator.CreatorDto;
import org.uhanov.dto.creator.CreatorSignUpDto;

import java.util.UUID;

public interface CreatorService {
    CreatorDto create(CreatorSignUpDto creatorSignUpDto);

    JwtAuthenticationResponse signIn(SignInDto signInDto);

    CreatorDto getById(UUID uuid);

    CreatorDto update(CreatorSignUpDto creatorSignUpDto);

    void delete(UUID uuid);

}
