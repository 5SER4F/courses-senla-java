package org.uhanov.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uhanov.model.AgeRating;

import java.util.UUID;

public interface AgeRatingRepository extends JpaRepository<AgeRating, UUID> {
}
