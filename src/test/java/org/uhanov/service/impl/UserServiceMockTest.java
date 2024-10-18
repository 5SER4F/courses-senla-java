package org.uhanov.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uhanov.dto.mapper.UserMapper;
import org.uhanov.dto.mapper.UserMapperImpl;
import org.uhanov.dto.user.UserAuthDto;
import org.uhanov.dto.user.UserFullDto;
import org.uhanov.model.User;
import org.uhanov.repository.api.UserRepository;
import org.uhanov.service.api.UserService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceMockTest {

    UserService userService;
    UserRepository userRepositoryMock;
    UserMapper userMapper;


    @BeforeEach
    public void init() {
        userRepositoryMock = mock(UserRepository.class);
        userMapper = new UserMapperImpl();

        userService = new UserServiceImpl(userRepositoryMock, userMapper);
    }

    @Test
    public void whenCreate_thenRepositoryCallSave() {
        UUID userUuid = UUID.randomUUID();
        UserAuthDto userAuthDto = UserAuthDto.builder()
                .build();

        when(userRepositoryMock.save(any(User.class)))
                .thenReturn(User.builder()
                        .id(userUuid)
                        .build());

        UserFullDto afterCreate = userService.create(userAuthDto);

        assertEquals(afterCreate.getId(), userUuid);

        verify(userRepositoryMock, times(1))
                .save(any(User.class));
    }


    @Test
    public void whenGetById_theRepositoryCallFindById() {
        UUID userUuid = UUID.randomUUID();

        when(userRepositoryMock.findById(userUuid))
                .thenReturn(Optional.of(User.builder().id(userUuid).build()));

        UserFullDto afterGet = userService.getById(userUuid);

        assertEquals(userUuid, afterGet.getId());

        verify(userRepositoryMock, times(1))
                .findById(userUuid);

    }

    @Test
    public void whenUpdate_thenRepositoryCallFindByIdAnd() {
        UUID userUuid = UUID.randomUUID();
        UserMapper userMapper = mock(UserMapper.class);
        UserAuthDto userAuthDto = UserAuthDto.builder()
                .id(userUuid)
                .build();

        User user = User.builder().id(userUuid).build();

        UserService userService = new UserServiceImpl(
                userRepositoryMock,
                userMapper
        );


        when(userRepositoryMock.findById(userUuid))
                .thenReturn(Optional.of(user));

        userService.update(userAuthDto);

        verify(userRepositoryMock, times(1))
                .findById(userUuid);

        verify(userRepositoryMock, times(1))
                .save(user);

        verify(userMapper, times(1))
                .updateUser(userAuthDto, user);

    }

    @Test
    public void whenDelete_thenRepositoryCallDelete() {
        UUID userUuid = UUID.randomUUID();
        userService.delete(userUuid);

        verify(userRepositoryMock, times(1))
                .deleteById(userUuid);
    }


}
