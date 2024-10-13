package org.uhanov.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.uhanov.dto.genre.GenreDto;
import org.uhanov.dto.genre.GenrePostDto;
import org.uhanov.dto.mapper.GenreMapper;
import org.uhanov.dto.mapper.GenreMapperImpl;
import org.uhanov.dto.mapper.StaffMapperImpl;
import org.uhanov.model.Genre;
import org.uhanov.model.Staff;
import org.uhanov.repository.api.GenreRepository;
import org.uhanov.repository.api.StaffRepository;
import org.uhanov.service.api.GenreService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class GenreServiceMockTest {

    GenreService genreService;
    GenreRepository genreRepositoryMock;
    StaffRepository staffRepositoryMock;
    GenreMapper genreMapper;

    @BeforeEach
    public void init() {
        genreMapper = initGenreMapper();
        genreRepositoryMock = mock(GenreRepository.class);
        staffRepositoryMock = mock(StaffRepository.class);

        genreService = new GenreServiceImpl(
                genreRepositoryMock,
                staffRepositoryMock,
                genreMapper
        );
    }

    @Test
    public void whenCreateGenre_thenGetStaffFromStaffRepoAndSave() {
        final UUID genreUuid = UUID.randomUUID();

        Staff staff = Staff.builder()
                .id(UUID.randomUUID())
                .firstname("name")
                .build();

        when(genreRepositoryMock.save(any(Genre.class)))
                .thenAnswer(
                        invocationOnMock -> {
                            Genre g = invocationOnMock
                                    .getArgument(0, Genre.class);
                            g.setId(genreUuid);
                            return g;
                        }
                );

        when(staffRepositoryMock.findById(staff.getId()))
                .thenReturn(Optional.of(staff));

        GenrePostDto genrePostDto = GenrePostDto.builder()
                .lastChangerId(staff.getId())
                .build();

        GenreDto afterCreate = genreService.create(genrePostDto);

        assertEquals(afterCreate.getId(), genreUuid);

        assertEquals(staff.getId(), afterCreate.getLastChanger().getId());

        Mockito.verify(staffRepositoryMock, times(1))
                .findById(staff.getId());
        verify(genreRepositoryMock, times(1))
                .save(any(Genre.class));
    }

    @Test
    public void whenGetById_thenReturnFromRepo() {
        UUID genreId = UUID.randomUUID();
        Genre genre = Genre.builder()
                .id(genreId)
                .build();
        when(genreRepositoryMock.findById(genreId))
                .thenReturn(Optional.of(genre));

        GenreDto fromRepo = genreService.getById(genreId);

        assertEquals(fromRepo.getId(), genreId);

        verify(genreRepositoryMock, times(1))
                .findById(genreId);
    }

    @Test
    public void whenUpdate_thenCallRepoUpdate() {
        GenreMapper genreMapper = mock(GenreMapper.class);
        GenreService genreService = new GenreServiceImpl(
                genreRepositoryMock,
                staffRepositoryMock,
                genreMapper
        );
        UUID genreUuid = UUID.randomUUID();
        GenrePostDto genrePostDto = GenrePostDto.builder()
                .id(genreUuid)
                .build();
        Genre GenreFromRepo = Genre.builder().id(genreUuid).build();

        when(genreRepositoryMock.findById(genreUuid))
                .thenReturn(Optional.of(GenreFromRepo));

        genreService.update(genrePostDto);

        verify(genreRepositoryMock, times(1))
                .findById(genreUuid);
        verify(genreMapper, times(1))
                .updateGenre(genrePostDto, GenreFromRepo);
    }

    @Test
    public void whenDelete_thenCallRepoDelete() {
        UUID genreUuid = UUID.randomUUID();
        genreService.delete(genreUuid);

        verify(genreRepositoryMock, times(1))
                .deleteById(genreUuid);
    }

    public static GenreMapper initGenreMapper() {
        GenreMapperImpl genreMapper = new GenreMapperImpl();
        try {
            var field = genreMapper.getClass()
                    .getDeclaredField("staffMapper");
            field.setAccessible(true);
            field.set(genreMapper, new StaffMapperImpl());
        } catch (Exception e) {
            System.out.println("Fail to create mapper for test");
            e.printStackTrace();
        }
        return genreMapper;
    }
}
