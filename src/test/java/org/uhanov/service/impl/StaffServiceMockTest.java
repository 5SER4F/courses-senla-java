package org.uhanov.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.uhanov.dto.mapper.StaffMapper;
import org.uhanov.dto.mapper.StaffMapperImpl;
import org.uhanov.dto.staff.StaffAuthDto;
import org.uhanov.dto.staff.StaffFullDto;
import org.uhanov.model.Creator;
import org.uhanov.model.Staff;
import org.uhanov.repository.api.StaffRepository;
import org.uhanov.service.api.StaffService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class StaffServiceMockTest {
    StaffService staffService;
    StaffRepository staffRepositoryMock;
    StaffMapper staffMapper;

    @BeforeEach
    public void init() {
        staffRepositoryMock = mock(StaffRepository.class);
        staffMapper = new StaffMapperImpl();

        staffService = new StaffServiceImpl(
                staffRepositoryMock,
                staffMapper
        );
    }

    @Test
    public void whenCreate_thenRepositoryCallSave() {
        final UUID staffUuid = UUID.randomUUID();
        StaffAuthDto staffAuthDto = StaffAuthDto.builder()
                .build();

        when(staffRepositoryMock.save(any(Staff.class)))
                .thenReturn(Staff.builder()
                        .id(staffUuid)
                        .build());

        StaffFullDto afterCreate = staffService.create(staffAuthDto);

        assertEquals(afterCreate.getId(), staffUuid);

        verify(staffRepositoryMock, times(1))
                .save(any(Staff.class));
    }


    @Test
    public void whenGetById_theRepositoryCallFindById() {
        final UUID staffUuid = UUID.randomUUID();

        when(staffRepositoryMock.findById(staffUuid))
                .thenReturn(Optional.of(Staff.builder().id(staffUuid).build()));

        StaffFullDto afterGet = staffService.getById(staffUuid);

        assertEquals(staffUuid, afterGet.getId());

        verify(staffRepositoryMock, times(1))
                .findById(staffUuid);

    }

    @Test
    public void whenUpdate_thenRepositoryCallFindByIdAnd() {
       final UUID staffUuid = UUID.randomUUID();
        StaffMapper creatorMapper = mock(StaffMapper.class);
        StaffAuthDto creatorAuthDto = StaffAuthDto.builder()
                .id(staffUuid)
                .build();

        Staff staff = Staff.builder().id(staffUuid).build();

        StaffService staffService = new StaffServiceImpl(
                staffRepositoryMock,
                creatorMapper
        );


        when(staffRepositoryMock.findById(staffUuid))
                .thenReturn(Optional.of(staff));

        staffService.update(creatorAuthDto);

        verify(staffRepositoryMock, times(1))
                .findById(staffUuid);

        verify(staffRepositoryMock, times(1))
                .update(staff);

        verify(creatorMapper, times(1))
                .updateStaff(creatorAuthDto, staff);

    }

    @Test
    public void whenDelete_thenRepositoryCallDelete() {
       final UUID staffUuid = UUID.randomUUID();
        staffService.delete(staffUuid);

        verify(staffRepositoryMock, times(1))
                .deleteById(staffUuid);
    }


}
