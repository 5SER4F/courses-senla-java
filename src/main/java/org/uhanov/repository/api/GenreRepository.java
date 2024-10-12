package org.uhanov.repository.api;

import org.uhanov.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GenreRepository extends CrudRepository<Genre> {
    List<Genre> findByStaffId(UUID uuid);

    Optional<Genre> findByIdEager(UUID uuid);

    List<Genre> findGenresByProductId(UUID productId);

    List<Genre> getGenresByIds(List<UUID> genreIds);
}
