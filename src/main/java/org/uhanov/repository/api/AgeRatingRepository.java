package org.uhanov.repository.api;

import org.uhanov.model.AgeRating;

import java.util.Optional;
import java.util.UUID;

public interface AgeRatingRepository extends CrudRepository<AgeRating> {
    Optional<AgeRating> findByIdEager(UUID uuid);
}
