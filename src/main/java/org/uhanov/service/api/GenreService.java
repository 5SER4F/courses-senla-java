package org.uhanov.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.uhanov.dto.genre.GenreDto;
import org.uhanov.dto.genre.GenrePostDto;

import java.util.UUID;

public interface GenreService {


    GenreDto create(GenrePostDto genreDto);


    GenreDto getById(UUID uuid);


    GenreDto update(GenrePostDto genreDto);


    void delete(UUID uuid);

    Page<GenreDto> getAll(Pageable pageable);
}
