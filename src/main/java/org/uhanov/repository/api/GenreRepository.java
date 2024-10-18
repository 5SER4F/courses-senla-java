package org.uhanov.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uhanov.model.AgeRating;
import org.uhanov.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GenreRepository extends JpaRepository<Genre, UUID>
// CrudRepository<Genre>
{
//    List<Genre> findByStaffId(UUID uuid);

//    Optional<Genre> findByIdEager(UUID uuid);

    List<Genre> findGenresByProductsId(UUID productId);

    default List<Genre> getGenresByIds(List<UUID> genreIds) {
     return findAllById(genreIds);
    }
}
