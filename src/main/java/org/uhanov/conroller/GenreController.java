package org.uhanov.conroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.uhanov.dto.genre.GenreDto;
import org.uhanov.dto.genre.GenrePostDto;
import org.uhanov.service.api.GenreService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.UUID;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Slf4j
public class GenreController {
    private final GenreService service;

    @PreAuthorize("hasAnyAuthority('STAFF')")
    @PostMapping
    public ResponseEntity<GenreDto> create(
            @RequestBody GenrePostDto dto
    ) {
        log.info("Создание жанра:" + dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    @PreAuthorize("hasAnyAuthority('STAFF')")
    @PatchMapping("/{genreId}")
    public ResponseEntity<GenreDto> update(
            @PathVariable("genreId") UUID genreId,
            @RequestBody GenrePostDto dto
    ) {
        dto.setId(genreId);
        log.info("Обновление жанра" + dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.update(dto));
    }


    @PreAuthorize("hasAnyAuthority('STAFF')")
    @DeleteMapping("/{genreId}")
    public ResponseEntity delete(
            @PathVariable("genreId") UUID genreId
    ) {
        log.info("Удаление жанра id=" + genreId);
        service.delete(genreId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }


    @GetMapping("/{genreId}")
    public ResponseEntity<GenreDto> get(
            @PathVariable("genreId") UUID genreId
    ) {
        log.info("Запрос жанра id=" + genreId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getById(genreId));
    }

    @GetMapping
    public ResponseEntity<Page<GenreDto>> getAll(
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @PositiveOrZero
            int page,
            @RequestParam(name = "size", required = false, defaultValue = "10")
            @Positive
            int size
    ) {
        log.info(String.format(
                "Запрос всех жанров с пагинацией page=%d, size=%d", page, size
        ));
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAll(PageRequest.of(page, size)));
    }


}
