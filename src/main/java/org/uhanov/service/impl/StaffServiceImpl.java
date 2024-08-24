package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uhanov.dto.StaffDTO;
import org.uhanov.dto.mapper.StaffMapper;
import org.uhanov.exception.EntityNotFoundException;
import org.uhanov.exception.PatchWithoutIdException;
import org.uhanov.model.Staff;
import org.uhanov.model.patcher.StaffPatcher;
import org.uhanov.repository.StaffRepositoryMock;
import org.uhanov.service.api.StaffService;

import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {
    private final StaffRepositoryMock repository;
    private final StaffMapper staffMapper;
    private final StaffPatcher patcher;

    @Override
    public StaffDTO create(StaffDTO dto) {
        return staffMapper.toDto(
                repository.addEntity(
                        staffMapper.toModel(dto)
                )
        );
    }

    @Override
    public StaffDTO getById(UUID uuid) {
        return staffMapper.toDto(getEntityById(uuid));
    }

    @Override
    public StaffDTO update(StaffDTO dto) {
        Staff oldStaff = getEntityById(dto.getId());
        Staff patch = staffMapper.toModel(dto);
        if (patch.getId() == null) {
            throw new PatchWithoutIdException();
        }
        Staff patchedStaff = patcher.patchEntity(oldStaff, patch);
        repository.addEntity(patchedStaff);
        return staffMapper.toDto(patchedStaff);
    }

    @Override
    public boolean delete(UUID uuid) {
        return repository.removeByUUID(uuid);
    }

    private Staff getEntityById(UUID uuid) {
        return repository.getByUUID(uuid)
                .orElseThrow(EntityNotFoundException::new);
    }
}
