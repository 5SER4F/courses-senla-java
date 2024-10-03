package org.uhanov.service.impl;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.dto.AgeRatingDTO;
import org.uhanov.dto.mapper.AgeRatingMapper;
import org.uhanov.exception.EntityNotFoundException;
import org.uhanov.model.AgeRating;
import org.uhanov.repository.api.AgeRatingRepository;
import org.uhanov.service.api.AgeRatingService;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@Data
@RequiredArgsConstructor
public class AgeRatingServiceImpl implements AgeRatingService {
    private final AgeRatingRepository repository;
    private final AgeRatingMapper ageRatingMapper;

    @Override
    public AgeRatingDTO create(AgeRatingDTO ageRatingDTO) {
        return ageRatingMapper.toShortDto(
                repository.save(
                        ageRatingMapper.toModel(ageRatingDTO)
                )
        );
    }

    @Transactional(readOnly = true)
    @Override
    public AgeRatingDTO getById(UUID uuid) {
        return ageRatingMapper.toShortDto(
                getEntityById(uuid)
        );
    }

    @Override
    public void update(AgeRatingDTO ageRatingDTO) {
        AgeRating ageRating = getEntityById(ageRatingDTO.getId());
        ageRatingMapper.updateAgeRating(ageRatingDTO, ageRating);
        repository.save(ageRating);
    }

    @Override
    public boolean delete(UUID uuid) {
        repository.deleteById(uuid);
        return true;
    }

    private AgeRating getEntityById(UUID uuid) {
        Optional<AgeRating> ageRating = repository.findById(uuid);
        return ageRating
                .orElseThrow(EntityNotFoundException::new);
    }
}
