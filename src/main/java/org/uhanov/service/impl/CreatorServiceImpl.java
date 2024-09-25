package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.dto.CreatorAuthDTO;
import org.uhanov.dto.CreatorGetFullDto;
import org.uhanov.dto.mapper.CreatorMapper;
import org.uhanov.exception.EntityNotFoundException;
import org.uhanov.model.Creator;
import org.uhanov.repository.api.CreatorRepository;
import org.uhanov.service.api.CreatorService;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@Data
@RequiredArgsConstructor
public class CreatorServiceImpl implements CreatorService {
    private final CreatorRepository repository;
    private final CreatorMapper creatorMapper;

    @Override
    public CreatorGetFullDto create(CreatorAuthDTO creatorAuthDTO) {
        return creatorMapper.toFullGETDto(
                repository.save(
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
        creatorMapper.updateCreator(creatorAuthDTO, creator);
        repository.save(creator);
    }

    @Override
    public boolean delete(UUID uuid) {
        repository.deleteById(uuid);
        return true;
    }

    private Creator getEntityById(UUID uuid) {
        Optional<Creator> creator = repository.findById(uuid);
        return creator
                .orElseThrow(EntityNotFoundException::new);
    }
}
