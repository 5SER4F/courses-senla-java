package org.uhanov.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product implements EntityWithUUID {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "creator_id", nullable = false)
    private UUID creatorId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "date_added")
    private LocalDateTime dateAdded;
    @Column(name = "age_rating_id")
    private UUID ageRatingId;
    @Column(name = "price", nullable = false)
    private BigDecimal price;
}
