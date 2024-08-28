package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uhanov.dto.UserAuthDTO;
import org.uhanov.dto.UserFullDTO;
import org.uhanov.dto.mapper.UserMapper;
import org.uhanov.exception.EntityNotFoundException;
import org.uhanov.model.User;
import org.uhanov.model.patcher.UserPatcher;
import org.uhanov.repository.UserRepositoryMock;
import org.uhanov.service.api.UserService;

import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepositoryMock repository;
    private final UserMapper userMapper;
    private final UserPatcher patcher;

    @Override
    public UserFullDTO create(UserAuthDTO dto) {
        return userMapper.toFullDto(
                repository.saveEntity(
                        userMapper.authToModel(dto)
                )
        );
    }

    @Override
    public UserFullDTO getById(UUID uuid) {
        return userMapper.toFullDto(getEntityById(uuid));
    }

    @Override
    public void update(UserAuthDTO dto) {
        User oldUser = getEntityById(dto.getId());
        User patch = userMapper.authToModel(dto);
        User patchedUser = patcher.patchEntity(oldUser, patch);
        repository.saveEntity(patchedUser);
    }

    @Override
    public boolean delete(UUID uuid) {
        return repository.removeByUUID(uuid);
    }

    private User getEntityById(UUID uuid) {
        return repository.getByUUID(uuid)
                .orElseThrow(EntityNotFoundException::new);
    }
}
