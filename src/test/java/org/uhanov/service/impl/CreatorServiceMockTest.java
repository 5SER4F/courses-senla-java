package org.uhanov.service.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.uhanov.dto.creator.CreatorDto;
import org.uhanov.dto.creator.CreatorSignUpDto;
import org.uhanov.dto.mapper.CreatorMapper;
import org.uhanov.dto.mapper.CreatorMapperImpl;
import org.uhanov.model.Creator;
import org.uhanov.repository.api.CreatorRepository;
import org.uhanov.security.JwtUtil;
import org.uhanov.service.api.CreatorService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreatorServiceMockTest {

    CreatorService creatorService;
    CreatorRepository creatorRepositoryMock;
    CreatorMapper creatorMapper;
    JwtUtil jwtUtils;
    AuthenticationManager authenticationManager;

    @BeforeEach
    public void init() {
        creatorMapper = new CreatorMapperImpl();
        creatorRepositoryMock = mock(CreatorRepository.class);
        jwtUtils = mock(JwtUtil.class);
        authenticationManager = mock(AuthenticationManager.class);

        creatorService = new CreatorServiceImpl(
                creatorRepositoryMock,
                creatorMapper,
                jwtUtils,
                authenticationManager

        );
    }

    @Test
    public void whenCreate_thenRepositoryCallSave() {
        UUID creatorUuid = UUID.randomUUID();
        CreatorSignUpDto creatorSignUpDto = CreatorSignUpDto.builder()
                .build();

        when(creatorRepositoryMock.save(any(Creator.class)))
                .thenReturn(Creator.builder()
                        .id(creatorUuid)
                        .name(creatorSignUpDto.getName())
                        .build());

        CreatorDto afterCreate = creatorService.create(creatorSignUpDto);

        assertEquals(afterCreate.getId(), creatorUuid);

        verify(creatorRepositoryMock, times(1))
                .save(any(Creator.class));
    }


    @Test
    public void whenGetById_theRepositoryCallFindById() {
        UUID creatorUuid = UUID.randomUUID();

        when(creatorRepositoryMock.findById(creatorUuid))
                .thenReturn(Optional.of(Creator.builder().id(creatorUuid).build()));

        CreatorDto afterGet = creatorService.getById(creatorUuid);

        assertEquals(creatorUuid, afterGet.getId());

        verify(creatorRepositoryMock, times(1))
                .findById(creatorUuid);

    }

    @Test
    public void whenUpdate_thenRepositoryCallFindByIdAnd() {
        UUID creatorUuid = UUID.randomUUID();
        CreatorMapper creatorMapper = mock(CreatorMapper.class);
        CreatorSignUpDto creatorSignUpDto = CreatorSignUpDto.builder()
                .id(creatorUuid)
                .build();

        Creator creator = Creator.builder().id(creatorUuid).build();

        CreatorService creatorService = new CreatorServiceImpl(
                creatorRepositoryMock,
                creatorMapper,
                jwtUtils,
                authenticationManager
        );


        when(creatorRepositoryMock.findById(creatorUuid))
                .thenReturn(Optional.of(creator));

        creatorService.update(creatorSignUpDto);

        verify(creatorRepositoryMock, times(1))
                .findById(creatorUuid);

        verify(creatorRepositoryMock, times(1))
                .save(creator);

        verify(creatorMapper, times(1))
                .updateCreator(creatorSignUpDto, creator);

    }

    @Test
    public void whenDelete_thenRepositoryCallDelete() {
        UUID creatorUuid = UUID.randomUUID();
        creatorService.delete(creatorUuid);

        verify(creatorRepositoryMock, times(1))
                .deleteById(creatorUuid);
    }


}
