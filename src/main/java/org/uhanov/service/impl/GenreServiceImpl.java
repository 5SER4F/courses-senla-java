package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uhanov.dto.GenreDTO;
import org.uhanov.dto.mapper.GenreMapper;
import org.uhanov.exception.EntityNotFoundException;
import org.uhanov.model.Genre;
import org.uhanov.model.patcher.GenrePatcher;
import org.uhanov.repository.inMemory.GenreRepositoryMock;
import org.uhanov.service.api.GenreService;

import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepositoryMock repository;
    private final GenreMapper genreMapper;
    private final GenrePatcher patcher;

    @Override
    public GenreDTO create(GenreDTO genreDTO) {
        return genreMapper.toDto(
                repository.saveEntity(
                        genreMapper.toModel(genreDTO)
                )
        );
    }

    @Override
    public GenreDTO getById(UUID uuid) {
        return genreMapper.toDto(getEntityById(uuid));
    }

    @Override
    public void update(GenreDTO genreDTO) {
        Genre genre = getEntityById(genreDTO.getId());
        patcher.patchEntity(genre, genreDTO);
        repository.saveEntity(genre);
    }

    @Override
    public boolean delete(UUID uuid) {
        return getRepository().removeByUUID(uuid);
    }

    private Genre getEntityById(UUID uuid) {
        return repository.getByUUID(uuid)
                .orElseThrow(EntityNotFoundException::new);
    }
}
