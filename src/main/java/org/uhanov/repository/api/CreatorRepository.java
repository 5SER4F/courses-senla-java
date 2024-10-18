package org.uhanov.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uhanov.model.AgeRating;
import org.uhanov.model.Creator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CreatorRepository
        extends JpaRepository<Creator, UUID>
// CrudRepository<Creator>
{
//    List<Creator> findByName(String nameSubString);
//
//    Optional<Creator> findByIdEager(UUID uuid);

}
