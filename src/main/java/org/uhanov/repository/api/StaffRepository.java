package org.uhanov.repository.api;

import org.springframework.stereotype.Repository;
import org.uhanov.model.Staff;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StaffRepository extends CrudRepository<Staff> {
    Optional<Staff> findStaffByIdEager(UUID uuid);

}
