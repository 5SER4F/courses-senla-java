package org.uhanov.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product implements EntityWithUUID {
    private UUID id;
    private UUID creatorId;
    private String name;
    private Timestamp dateAdded;
    private UUID ageRatingId;
    private Double price;
}
