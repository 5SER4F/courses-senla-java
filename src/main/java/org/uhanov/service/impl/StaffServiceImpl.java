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
import org.uhanov.dto.mapper.StaffMapper;
import org.uhanov.dto.staff.StaffFullDto;
import org.uhanov.dto.staff.StaffSignUpDto;
import org.uhanov.exception.ResourceNotFoundException;
import org.uhanov.model.Staff;
import org.uhanov.repository.api.StaffRepository;
import org.uhanov.security.JwtUtil;
import org.uhanov.service.api.StaffService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@Data
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {
    private final StaffRepository repository;
    private final StaffMapper staffMapper;
    private final JwtUtil jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public StaffFullDto create(StaffSignUpDto dto) {
        return staffMapper.toFullDto(
                repository.save(
                        staffMapper.authToModel(dto)
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

        UserDetails user = repository.findStaffDetailsByName(signInDto.getUsername());

        String jwt = jwtUtils.generateToken(user);

        return new JwtAuthenticationResponse(jwt);
    }

    @Transactional(readOnly = true)
    @Override
    public StaffFullDto getById(UUID uuid) {
        return staffMapper.toFullDto(getEntityById(uuid));
    }

    @Override
    public StaffFullDto update(StaffSignUpDto dto) {
        Staff staff = getEntityById(dto.getId());
        staffMapper.updateStaff(dto, staff);
        return staffMapper.toFullDto(
                repository.save(staff)
        );
    }

    @Override
    public void delete(UUID uuid) {
        repository.deleteById(uuid);
    }

    private Staff getEntityById(UUID uuid) {
        Optional<Staff> staff = repository.findById(uuid);
        return staff.orElseThrow(
                ResourceNotFoundException::new
        );
    }
}
