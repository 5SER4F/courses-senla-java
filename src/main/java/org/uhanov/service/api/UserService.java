package org.uhanov.service.api;

import org.uhanov.dto.JwtAuthenticationResponse;
import org.uhanov.dto.SignInDto;
import org.uhanov.dto.creator.CreatorDto;
import org.uhanov.dto.creator.CreatorPostDto;
import org.uhanov.dto.staff.StaffFullDto;
import org.uhanov.dto.staff.StaffPostDto;
import org.uhanov.dto.user.CustomerFullDto;
import org.uhanov.dto.user.CustomerPostDto;

import java.util.UUID;

public interface UserService {
    JwtAuthenticationResponse signIn(SignInDto signInDto);

    CustomerFullDto signUpCustomer(CustomerPostDto customerPostDto);

    CreatorDto signUpCreator(CreatorPostDto creatorPostDto);

    StaffFullDto signUpStaff(StaffPostDto staffPostDto);

    void deleteAccount(UUID uuid);

}
