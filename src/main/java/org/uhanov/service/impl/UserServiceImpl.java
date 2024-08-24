package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uhanov.dto.UserDTO;
import org.uhanov.dto.mapper.UserMapper;
import org.uhanov.exception.EntityNotFoundException;
import org.uhanov.exception.PatchWithoutIdException;
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
    public UserDTO create(UserDTO dto) {
        return userMapper.toDto(
                repository.addEntity(
                        userMapper.toModel(dto)
                )
        );
    }

    @Override
    public UserDTO getById(UUID uuid) {
        return userMapper.toDto(getEntityById(uuid));
    }

    @Override
    public UserDTO update(UserDTO dto) {
        User oldUser = getEntityById(dto.getId());
        User patch = userMapper.toModel(dto);
        if (patch.getId() == null) {
            throw new PatchWithoutIdException();
        }
        User patchedUser = patcher.patchEntity(oldUser, patch);
        repository.addEntity(patchedUser);
        return userMapper.toDto(patchedUser);
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
