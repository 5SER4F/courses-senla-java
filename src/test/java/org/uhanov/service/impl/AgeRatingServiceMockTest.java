package org.uhanov.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.uhanov.dto.agerating.AgeRatingDto;
import org.uhanov.dto.agerating.AgeRatingPostDto;
import org.uhanov.dto.mapper.AgeRatingMapper;
import org.uhanov.dto.mapper.AgeRatingMapperImpl;
import org.uhanov.dto.mapper.StaffMapperImpl;
import org.uhanov.model.AgeRating;
import org.uhanov.model.Staff;
import org.uhanov.repository.api.AgeRatingRepository;
import org.uhanov.repository.api.StaffRepository;
import org.uhanov.service.api.AgeRatingService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class AgeRatingServiceMockTest {
    AgeRatingService ageRatingService;

    AgeRatingMapper ageRatingMapper;
    AgeRatingRepository ageRatingRepositoryMock;
    StaffRepository staffRepositoryMock;


    @BeforeEach
    public void init() {
        ageRatingMapper = initAgeRatingMapper();
        ageRatingRepositoryMock = Mockito.mock(AgeRatingRepository.class);
        staffRepositoryMock = Mockito.mock(StaffRepository.class);

        ageRatingService = new AgeRatingServiceImpl(
                ageRatingRepositoryMock,
                staffRepositoryMock,
                ageRatingMapper
        );

    }

    @Test
    public void whenCreateAgeRating_thenGetStaffFromStaffRepoAndSave() {
        final UUID ageRatingUUId = UUID.randomUUID();
        Staff staff = Staff.builder()
                .id(UUID.randomUUID())
                .build();
        Mockito
                .when(ageRatingRepositoryMock.save(any(AgeRating.class)))
                .thenAnswer(
                        invocationOnMock -> {
                            AgeRating ar = invocationOnMock
                                    .getArgument(0, AgeRating.class);
                            ar.setId(ageRatingUUId);
                            return ar;
                        }
                );
        Mockito
                .when(staffRepositoryMock.findById(staff.getId()))
                .thenReturn(Optional.of(staff));

        AgeRatingPostDto ageRatingPostDto = AgeRatingPostDto.builder()
                .lastChangerId(staff.getId())
                .build();

        AgeRatingDto afterCreate = ageRatingService.create(ageRatingPostDto);

        assertEquals(afterCreate.getId(), ageRatingUUId);

        assertEquals(staff.getId(), afterCreate.getLastChanger().getId());

        Mockito.verify(staffRepositoryMock, times(1))
                .findById(staff.getId());
        Mockito.verify(ageRatingRepositoryMock, times(1))
                .save(any(AgeRating.class));

    }

    @Test
    public void whenGetById_thenReturnFromRepo() {
        final UUID ageRatingId = UUID.randomUUID();
        AgeRating ageRating = AgeRating.builder()
                .id(ageRatingId)
                .build();
        when(ageRatingRepositoryMock.findById(ageRatingId))
                .thenReturn(Optional.of(ageRating));

        AgeRatingDto fromRepo = ageRatingService.getById(ageRatingId);

        assertEquals(fromRepo.getId(), ageRatingId);

        verify(ageRatingRepositoryMock, times(1))
                .findById(ageRatingId);

    }

    @Test
    public void whenUpdate_thenCallRepoUpdate() {
        AgeRatingMapper ageRatingMapper1 = mock(AgeRatingMapper.class);
        AgeRatingService ratingService = new AgeRatingServiceImpl(
                ageRatingRepositoryMock,
                staffRepositoryMock,
                ageRatingMapper1
        );
        UUID ratingUuid = UUID.randomUUID();
        AgeRatingPostDto ageRatingPostDto = AgeRatingPostDto.builder()
                .id(ratingUuid)
                .build();
        AgeRating ageRatingFromRepo = AgeRating.builder().id(ratingUuid).build();

        when(ageRatingRepositoryMock.findById(ratingUuid))
                .thenReturn(Optional.of(ageRatingFromRepo));

        ratingService.update(ageRatingPostDto);

        verify(ageRatingRepositoryMock, times(1))
                .findById(ratingUuid);
        verify(ageRatingMapper1, times(1))
                .updateAgeRating(ageRatingPostDto, ageRatingFromRepo);
    }

    @Test
    public void whenDelete_thenCallRepoDelete() {
        final UUID ratingUuid = UUID.randomUUID();
        ageRatingService.delete(ratingUuid);

        verify(ageRatingRepositoryMock, times(1))
                .deleteById(ratingUuid);
    }

    public static AgeRatingMapper initAgeRatingMapper() {
        AgeRatingMapperImpl ageRatingMapper = new AgeRatingMapperImpl();
        try {
            var field = ageRatingMapper.getClass()
                    .getDeclaredField("staffMapper");
            field.setAccessible(true);
            field.set(ageRatingMapper, new StaffMapperImpl());
        } catch (Exception e) {
            System.out.println("Fail to create mapper for test");
            e.printStackTrace();
        }
        return ageRatingMapper;
    }

}
