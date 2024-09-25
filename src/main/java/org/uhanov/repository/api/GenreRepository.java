package org.uhanov.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uhanov.model.Genre;

import java.util.UUID;

public interface GenreRepository extends JpaRepository<Genre, UUID> {
}
