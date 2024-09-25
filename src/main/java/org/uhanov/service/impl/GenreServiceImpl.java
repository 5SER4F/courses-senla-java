package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.dto.GenreDTO;
import org.uhanov.dto.mapper.GenreMapper;
import org.uhanov.exception.EntityNotFoundException;
import org.uhanov.model.Genre;
import org.uhanov.repository.api.GenreRepository;
import org.uhanov.service.api.GenreService;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@Data
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository repository;
    private final GenreMapper genreMapper;

    @Override
    public GenreDTO create(GenreDTO genreDTO) {
        return genreMapper.toDto(
                repository.save(
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
        genreMapper.updateGenre(genreDTO, genre);
        repository.save(genre);
    }

    @Override
    public boolean delete(UUID uuid) {
        getRepository().deleteById(uuid);
        return false;
    }

    private Genre getEntityById(UUID uuid) {
        Optional<Genre> genre = repository.findById(uuid);
        return genre
                .orElseThrow(EntityNotFoundException::new);
    }
}
