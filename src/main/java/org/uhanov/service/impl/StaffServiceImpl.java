package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.dto.StaffAuthDTO;
import org.uhanov.dto.StaffFullDTO;
import org.uhanov.dto.mapper.StaffMapper;
import org.uhanov.exception.EntityNotFoundException;
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
    public StaffFullDTO create(StaffAuthDTO dto) {
        return staffMapper.toFullDTO(
                repository.save(
                        staffMapper.authToModel(dto)
                )
        );
    }

    @Transactional(readOnly = true)
    @Override
    public StaffFullDTO getById(UUID uuid) {
        return staffMapper.toFullDTO(getEntityById(uuid));
    }

    @Override
    public void update(StaffAuthDTO dto) {
        Staff staff = getEntityById(dto.getId());
        staffMapper.updateStaff(dto, staff);
        repository.save(staff);
    }

    @Override
    public boolean delete(UUID uuid) {
        repository.deleteById(uuid);
        return true;
    }

    private Staff getEntityById(UUID uuid) {
        System.out.println("QQQQQQQQQQQQ" + uuid);

        Optional<Staff> staff = repository.findById(uuid);
        return staff.orElseThrow(
                EntityNotFoundException::new
        );
    }
}
