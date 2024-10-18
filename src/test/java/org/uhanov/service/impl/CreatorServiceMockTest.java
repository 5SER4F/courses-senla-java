package org.uhanov.service.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uhanov.dto.creator.CreatorAuthDto;
import org.uhanov.dto.creator.CreatorDto;
import org.uhanov.dto.mapper.CreatorMapper;
import org.uhanov.dto.mapper.CreatorMapperImpl;
import org.uhanov.model.Creator;
import org.uhanov.repository.api.CreatorRepository;
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

    @BeforeEach
    public void init() {
        creatorMapper = new CreatorMapperImpl();
        creatorRepositoryMock = mock(CreatorRepository.class);

        creatorService = new CreatorServiceImpl(
                creatorRepositoryMock,
                creatorMapper
        );
    }

    @Test
    public void whenCreate_thenRepositoryCallSave() {
        UUID creatorUuid = UUID.randomUUID();
        CreatorAuthDto creatorAuthDto = CreatorAuthDto.builder()
                .build();

        when(creatorRepositoryMock.save(any(Creator.class)))
                .thenReturn(Creator.builder()
                        .id(creatorUuid)
                        .name(creatorAuthDto.getName())
                        .build());

        CreatorDto afterCreate = creatorService.create(creatorAuthDto);

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
        CreatorAuthDto creatorAuthDto = CreatorAuthDto.builder()
                .id(creatorUuid)
                .build();

        Creator creator = Creator.builder().id(creatorUuid).build();

        CreatorService creatorService = new CreatorServiceImpl(
                creatorRepositoryMock,
                creatorMapper
        );


        when(creatorRepositoryMock.findById(creatorUuid))
                .thenReturn(Optional.of(creator));

        creatorService.update(creatorAuthDto);

        verify(creatorRepositoryMock, times(1))
                .findById(creatorUuid);

        verify(creatorRepositoryMock, times(1))
                .save(creator);

        verify(creatorMapper, times(1))
                .updateCreator(creatorAuthDto, creator);

    }

    @Test
    public void whenDelete_thenRepositoryCallDelete() {
        UUID creatorUuid = UUID.randomUUID();
        creatorService.delete(creatorUuid);

        verify(creatorRepositoryMock, times(1))
                .deleteById(creatorUuid);
    }


}
