package org.uhanov.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.uhanov.exception.ResourceNotFoundException;
import org.uhanov.model.AgeRating;
import org.uhanov.model.Staff;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID>
// CrudRepository<Staff>
{
//    Optional<Staff> findStaffByIdEager(UUID uuid);

    Optional<Staff> findByUsername(String username);

    default UserDetails findStaffDetailsByName(String name) {
        return findByUsername(name)
                .orElseThrow(ResourceNotFoundException::new);
    }

    boolean existsByUsername(String username);

}
