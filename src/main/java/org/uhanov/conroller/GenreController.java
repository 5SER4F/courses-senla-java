package org.uhanov.conroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uhanov.dto.genre.GenreDto;
import org.uhanov.dto.genre.GenrePostDto;
import org.uhanov.service.api.GenreService;

import java.util.UUID;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService service;

    @PostMapping("")
    public ResponseEntity<GenreDto> create(
            @RequestBody GenrePostDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    @PatchMapping("/{genreId}")
    public ResponseEntity update(
            @PathVariable("genreId") UUID genreId,
            @RequestBody GenrePostDto dto
    ) {
        dto.setId(genreId);
        service.update(dto);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }


    @DeleteMapping("/{genreId}")
    public ResponseEntity delete(
            @PathVariable("genreId") UUID genreId
    ) {
        service.delete(genreId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }


    @GetMapping("/{genreId}")
    public ResponseEntity<GenreDto> get(
            @PathVariable("genreId") UUID genreId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getById(genreId));
    }

}
