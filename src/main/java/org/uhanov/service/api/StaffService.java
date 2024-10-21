package org.uhanov.service.api;

import org.uhanov.dto.JwtAuthenticationResponse;
import org.uhanov.dto.SignInDto;
import org.uhanov.dto.staff.StaffFullDto;
import org.uhanov.dto.staff.StaffSignUpDto;

import java.util.UUID;

public interface StaffService {

    StaffFullDto create(StaffSignUpDto dto);

    JwtAuthenticationResponse signIn(SignInDto signInDto);

    StaffFullDto getById(UUID uuid);

    StaffFullDto update(StaffSignUpDto dto);

    void delete(UUID uuid);

}
