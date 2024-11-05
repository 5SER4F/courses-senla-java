package org.uhanov.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uhanov.model.Genre;

import java.util.List;
import java.util.UUID;

@Repository
public interface GenreRepository extends JpaRepository<Genre, UUID> {
    default List<Genre> getGenresByIds(List<UUID> genreIds) {
        return findAllById(genreIds);
    }

    List<Genre> findAllByNameIn(List<String> name);

}
