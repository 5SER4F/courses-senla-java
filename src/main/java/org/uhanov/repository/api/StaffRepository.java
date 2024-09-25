package org.uhanov.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uhanov.model.Staff;

import java.util.UUID;

public interface StaffRepository extends JpaRepository<Staff, UUID> {
}
