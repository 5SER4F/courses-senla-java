package org.uhanov.service.impl;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.dto.agerating.AgeRatingDto;
import org.uhanov.dto.agerating.AgeRatingPostDto;
import org.uhanov.dto.mapper.AgeRatingMapper;
import org.uhanov.exception.ResourceNotFoundException;
import org.uhanov.model.AgeRating;
import org.uhanov.model.Staff;
import org.uhanov.repository.api.AgeRatingRepository;
import org.uhanov.repository.api.StaffRepository;
import org.uhanov.service.api.AgeRatingService;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@Data
@RequiredArgsConstructor
public class AgeRatingServiceImpl implements AgeRatingService {
    private final AgeRatingRepository repository;
    private final StaffRepository staffRepository;
    private final AgeRatingMapper ageRatingMapper;

    @Transactional
    @Override
    public AgeRatingDto create(AgeRatingPostDto ageRatingDto) {
        Staff creator = staffRepository.findById(ageRatingDto.getLastChangerId())
                .orElseThrow(ResourceNotFoundException::new);
        AgeRating newAgeRating = ageRatingMapper.postDtoToModel(ageRatingDto);
        newAgeRating.setLastChanger(creator);
        return ageRatingMapper.toDto(
                repository.save(newAgeRating)
        );
    }

    @Transactional(readOnly = true)
    @Override
    public AgeRatingDto getById(UUID uuid) {
        return ageRatingMapper.toDto(
                get(uuid)
        );
    }

    @Transactional
    @Override
    public AgeRatingDto update(AgeRatingPostDto ageRatingDto) {
        AgeRating ageRating = get(ageRatingDto.getId());
        ageRatingMapper.updateAgeRating(ageRatingDto, ageRating);
        if (ageRatingDto.getLastChangerId() != null) {
            ageRating.setLastChanger(
                    staffRepository.findById(ageRatingDto.getLastChangerId())
                            .orElseThrow(ResourceNotFoundException::new)
            );
        }
        return ageRatingMapper.toDto(
                repository.save(ageRating)
        );
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        repository.deleteById(uuid);
    }

    private AgeRating get(UUID uuid) {
        Optional<AgeRating> ageRating = repository.findById(uuid);
        return ageRating
                .orElseThrow(ResourceNotFoundException::new);
    }
}
