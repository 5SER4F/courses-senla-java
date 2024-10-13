package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.dto.mapper.StaffMapper;
import org.uhanov.dto.staff.StaffAuthDto;
import org.uhanov.dto.staff.StaffFullDto;
import org.uhanov.exception.ResourceNotFoundException;
import org.uhanov.model.Staff;
import org.uhanov.repository.api.StaffRepository;
import org.uhanov.service.api.StaffService;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@Data
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {
    private final StaffRepository repository;
    private final StaffMapper staffMapper;

    @Override
    public StaffFullDto create(StaffAuthDto dto) {
        return staffMapper.toFullDto(
                repository.save(
                        staffMapper.authToModel(dto)
                )
        );
    }

    @Transactional(readOnly = true)
    @Override
    public StaffFullDto getById(UUID uuid) {
        return staffMapper.toFullDto(getEntityById(uuid));
    }

    @Override
    public StaffFullDto update(StaffAuthDto dto) {
        Staff staff = getEntityById(dto.getId());
        staffMapper.updateStaff(dto, staff);
        return staffMapper.toFullDto(
                repository.update(staff)
        );
    }

    @Override
    public void delete(UUID uuid) {
        repository.deleteById(uuid);
    }

    private Staff getEntityById(UUID uuid) {
        Optional<Staff> staff = repository.findById(uuid);
        return staff.orElseThrow(
                ResourceNotFoundException::new
        );
    }
}
