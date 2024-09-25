package org.uhanov.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uhanov.model.Creator;

import java.util.UUID;

public interface CreatorRepository extends JpaRepository<Creator, UUID> {
}
