package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.dto.creator.CreatorAuthDto;
import org.uhanov.dto.creator.CreatorDto;
import org.uhanov.dto.mapper.CreatorMapper;
import org.uhanov.exception.ResourceNotFoundException;
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

    @Transactional
    @Override
    public CreatorDto create(CreatorAuthDto creatorAuthDto) {
        return creatorMapper.toDto(
                repository.save(
                        creatorMapper.authToModel(creatorAuthDto)
                )
        );
    }

    @Transactional(readOnly = true)
    @Override
    public CreatorDto getById(UUID uuid) {
        return creatorMapper.toDto(get(uuid));
    }

    @Transactional
    @Override
    public CreatorDto update(CreatorAuthDto creatorAuthDto) {
        Creator creator = get(creatorAuthDto.getId());
        creatorMapper.updateCreator(creatorAuthDto, creator);
        return creatorMapper.toDto(
                repository.update(creator)
        );
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        repository.deleteById(uuid);
    }

    private Creator get(UUID uuid) {
        Optional<Creator> creator = repository.findById(uuid);
        return creator
                .orElseThrow(ResourceNotFoundException::new);
    }
}
