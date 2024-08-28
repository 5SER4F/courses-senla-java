package org.uhanov.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product implements EntityWithUUID {
    private UUID id;
    private UUID creatorId;
    private String name;
    private LocalDateTime dateAdded;
    private UUID ageRatingId;
    private Double price;
}
