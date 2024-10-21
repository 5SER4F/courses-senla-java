package org.uhanov.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uhanov.model.AgeRating;

import java.util.UUID;

@Repository
public interface AgeRatingRepository extends JpaRepository<AgeRating, UUID> {
}
