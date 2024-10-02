package org.uhanov.repository.api;

import org.uhanov.model.Creator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CreatorRepository extends CrudRepository<Creator> {
    List<Creator> findByName(String nameSubString);

    Optional<Creator> findByIdEager(UUID uuid);

}
