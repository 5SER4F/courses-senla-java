package org.uhanov.service.api;

import org.uhanov.dto.GenreDTO;

import java.util.UUID;

public interface GenreService {


    GenreDTO create(GenreDTO genreDTO);


    GenreDTO getById(UUID uuid);


    void update(GenreDTO genreDTO);


    boolean delete(UUID uuid);
}
