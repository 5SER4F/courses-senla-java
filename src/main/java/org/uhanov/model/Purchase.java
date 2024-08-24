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
public class Purchase implements EntityWithUUID {
    private UUID id;
    private UUID userId;
    private UUID productId;
    private Double cost;
    private Timestamp purchaseDate;

}