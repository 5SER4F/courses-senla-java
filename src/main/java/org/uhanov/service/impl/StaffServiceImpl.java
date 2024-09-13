package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uhanov.dto.StaffAuthDTO;
import org.uhanov.dto.StaffFullDTO;
import org.uhanov.dto.mapper.StaffMapper;
import org.uhanov.exception.EntityNotFoundException;
import org.uhanov.model.Staff;
import org.uhanov.model.patcher.StaffPatcher;
import org.uhanov.repository.inMemory.StaffRepositoryMock;
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
    public StaffFullDTO create(StaffAuthDTO dto) {
        return staffMapper.toFullDTO(
                repository.saveEntity(
                        staffMapper.authToModel(dto)
                )
        );
    }

    @Override
    public StaffFullDTO getById(UUID uuid) {
        return staffMapper.toFullDTO(getEntityById(uuid));
    }

    @Override
    public void update(StaffAuthDTO dto) {
        Staff staff = getEntityById(dto.getId());
        patcher.patchEntity(staff, dto);
        repository.saveEntity(staff);
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
