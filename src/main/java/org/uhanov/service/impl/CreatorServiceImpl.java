package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uhanov.dto.CreatorDTO;
import org.uhanov.dto.mapper.CreatorMapper;
import org.uhanov.exception.EntityNotFoundException;
import org.uhanov.exception.PatchWithoutIdException;
import org.uhanov.model.Creator;
import org.uhanov.model.patcher.CreatorPatcher;
import org.uhanov.repository.CreatorRepositoryMock;
import org.uhanov.service.api.CreatorService;

import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor
public class CreatorServiceImpl implements CreatorService {
    private final CreatorRepositoryMock repository;
    private final CreatorMapper creatorMapper;
    private final CreatorPatcher patcher;

    @Override
    public CreatorDTO create(CreatorDTO creatorDTO) {
        return creatorMapper.toDto(
                repository.addEntity(
                        creatorMapper.toModel(creatorDTO)
                )
        );
    }

    @Override
    public CreatorDTO getById(UUID uuid) {
        return creatorMapper.toDto(getEntityById(uuid));
    }

    @Override
    public CreatorDTO update(CreatorDTO creatorDTO) {
        Creator oldCreator = getEntityById(creatorDTO.getId());
        Creator patch = creatorMapper.toModel(creatorDTO);
        if (patch.getId() == null) {
            throw new PatchWithoutIdException();
        }
        Creator patchedCreator = patcher.patchEntity(oldCreator, patch);
        repository.addEntity(patchedCreator);
        return creatorMapper.toDto(patchedCreator);
    }

    @Override
    public boolean delete(UUID uuid) {
        return repository.removeByUUID(uuid);
    }

    private Creator getEntityById(UUID uuid) {
        return repository.getByUUID(uuid)
                .orElseThrow(EntityNotFoundException::new);
    }
}
