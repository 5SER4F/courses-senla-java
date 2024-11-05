package org.uhanov.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.uhanov.exception.ResourceNotFoundException;
import org.uhanov.model.Creator;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface CreatorRepository extends JpaRepository<Creator, UUID> {
    Optional<Creator> findByName(String name);
    default UserDetails findCreatorDetailsByName(String name) {
        return findByName(name)
                .orElseThrow(ResourceNotFoundException::new);
    }

    Set<Creator> findAllByNameInIgnoreCase(Iterable<String> creatorNames);

}
