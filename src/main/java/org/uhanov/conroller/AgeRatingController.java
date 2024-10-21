package org.uhanov.conroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uhanov.dto.agerating.AgeRatingDto;
import org.uhanov.dto.agerating.AgeRatingPostDto;
import org.uhanov.service.api.AgeRatingService;

import java.util.UUID;

@RestController
@RequestMapping(path = "/age_ratings")
@RequiredArgsConstructor
public class AgeRatingController {
    private final AgeRatingService service;

    @PostMapping
    public ResponseEntity<AgeRatingDto> create(
            @RequestBody AgeRatingPostDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    @PatchMapping("/{ageRatingId}")
    public ResponseEntity<AgeRatingDto> update(
            @PathVariable("ageRatingId") UUID ageRatingId,
            @RequestBody AgeRatingPostDto dto
    ) {
        dto.setId(ageRatingId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.update(dto));
    }


    @DeleteMapping("/{ageRatingId}")
    public ResponseEntity delete(
            @PathVariable("ageRatingId") UUID ageRatingId
    ) {
        service.delete(ageRatingId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }


    @GetMapping("/{ageRatingId}")
    public ResponseEntity<AgeRatingDto> get(
            @PathVariable("ageRatingId") UUID ageRatingId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getById(ageRatingId));
    }
}
