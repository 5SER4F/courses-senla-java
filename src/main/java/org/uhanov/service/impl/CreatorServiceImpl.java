package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uhanov.dto.CreatorAuthDTO;
import org.uhanov.dto.CreatorGetFullDto;
import org.uhanov.dto.mapper.CreatorMapper;
import org.uhanov.exception.EntityNotFoundException;
import org.uhanov.model.Creator;
import org.uhanov.model.patcher.CreatorNewPatcher;
import org.uhanov.repository.inMemory.CreatorRepositoryMock;
import org.uhanov.service.api.CreatorService;

import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor
public class CreatorServiceImpl implements CreatorService {
    private final CreatorRepositoryMock repository;
    private final CreatorMapper creatorMapper;
    private final CreatorNewPatcher patcher;

    @Override
    public CreatorGetFullDto create(CreatorAuthDTO creatorAuthDTO) {
        return creatorMapper.toFullGETDto(
                repository.saveEntity(
                        creatorMapper.postDTOToModel(creatorAuthDTO)
                )
        );
    }

    @Override
    public CreatorGetFullDto getById(UUID uuid) {
        return creatorMapper.toFullGETDto(getEntityById(uuid));
    }

    @Override
    public void update(CreatorAuthDTO creatorAuthDTO) {
        Creator creator = getEntityById(creatorAuthDTO.getId());
        patcher.patchEntity(creator, creatorAuthDTO);
        repository.saveEntity(creator);
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
