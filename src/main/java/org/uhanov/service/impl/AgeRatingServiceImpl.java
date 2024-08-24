package org.uhanov.service.impl;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uhanov.dto.AgeRatingDTO;
import org.uhanov.dto.mapper.AgeRatingMapper;
import org.uhanov.exception.EntityNotFoundException;
import org.uhanov.exception.PatchWithoutIdException;
import org.uhanov.model.AgeRating;
import org.uhanov.model.patcher.AgeRatingPatcher;
import org.uhanov.repository.AgeRatingRepositoryMock;
import org.uhanov.service.api.AgeRatingService;

import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor
public class AgeRatingServiceImpl implements AgeRatingService {
    private final AgeRatingRepositoryMock repository;
    private final AgeRatingMapper ageRatingMapper;
    private final AgeRatingPatcher patcher;

    @Override
    public AgeRatingDTO create(AgeRatingDTO ageRatingDTO) {
        return ageRatingMapper.toDto(
                repository.addEntity(
                        ageRatingMapper.toModel(ageRatingDTO)
                )
        );
    }

    @Override
    public AgeRatingDTO getById(UUID uuid) {
        return ageRatingMapper.toDto(
                getEntityById(uuid)
        );
    }

    @Override
    public AgeRatingDTO update(AgeRatingDTO ageRatingDTO) {
        AgeRating oldAgeRating = getEntityById(ageRatingDTO.getId());
        AgeRating patch = ageRatingMapper.toModel(ageRatingDTO);
        if (patch.getId() == null) {
            throw new PatchWithoutIdException();
        }
        AgeRating patchedAgeRating = patcher.patchEntity(oldAgeRating, patch);
        repository.addEntity(patchedAgeRating);
        return ageRatingMapper.toDto(patchedAgeRating);
    }

    @Override
    public boolean delete(UUID uuid) {
        return repository.removeByUUID(uuid);
    }

    private AgeRating getEntityById(UUID uuid) {
        return repository.getByUUID(uuid)
                .orElseThrow(EntityNotFoundException::new);
    }
}
