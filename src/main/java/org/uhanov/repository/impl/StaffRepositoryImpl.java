package org.uhanov.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.uhanov.model.Staff;
import org.uhanov.repository.api.StaffRepository;
import org.uhanov.repository.dao.StaffDao;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StaffRepositoryImpl implements StaffRepository {
    private final StaffDao staffDao;

    @Override
    public Optional<Staff> findById(UUID id) {
        return staffDao.findById(id);
    }

    @Override
    public Staff save(Staff entity) {
        return staffDao.save(entity);
    }

    @Override
    public void deleteById(UUID id) {
        staffDao.deleteById(id);
    }

    @Override
    public Staff update(Staff e) {
        return staffDao.update(e);
    }

    @Override
    public Optional<Staff> findStaffByIdEager(UUID uuid) {
        return staffDao.findStaffByIdEager(uuid);
    }
}
