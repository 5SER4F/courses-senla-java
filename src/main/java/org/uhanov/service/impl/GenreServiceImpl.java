package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.dto.genre.GenreDto;
import org.uhanov.dto.genre.GenrePostDto;
import org.uhanov.dto.mapper.GenreMapper;
import org.uhanov.exception.ResourceNotFoundException;
import org.uhanov.model.Genre;
import org.uhanov.model.Staff;
import org.uhanov.repository.api.GenreRepository;
import org.uhanov.repository.api.StaffRepository;
import org.uhanov.service.api.GenreService;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@Data
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository repository;
    private final StaffRepository staffRepository;
    private final GenreMapper genreMapper;

    @Transactional
    @Override
    public GenreDto create(GenrePostDto genreDto) {
        Staff creator = staffRepository.findById(genreDto.getLastChangerId())
                .orElseThrow(ResourceNotFoundException::new);
        Genre newGenre = genreMapper.toModel(genreDto);
        newGenre.setLastChanger(creator);
        return genreMapper.toDto(
                repository.save(newGenre)
        );
    }

    @Transactional(readOnly = true)
    @Override
    public GenreDto getById(UUID uuid) {
        return genreMapper.toDto(
                getEntityById(uuid)
        );
    }

    @Transactional
    @Override
    public GenreDto update(GenrePostDto genreDto) {
        Genre genre = getEntityById(genreDto.getId());
        genreMapper.updateGenre(genreDto, genre);
        if (genreDto.getLastChangerId() != null) {
            genre.setLastChanger(
                    staffRepository.findById(genreDto.getLastChangerId())
                            .orElseThrow(ResourceNotFoundException::new)
            );
        }
        return genreMapper.toDto(
                repository.save(genre)
        );
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        repository.deleteById(uuid);
    }

    private Genre getEntityById(UUID uuid) {
        Optional<Genre> genre = repository.findById(uuid);
        return genre
                .orElseThrow(ResourceNotFoundException::new);
    }
}
