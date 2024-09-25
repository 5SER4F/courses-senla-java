package org.uhanov.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "age_rating")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AgeRating implements EntityWithUUID {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "add_by", nullable = false)
    private UUID lastChanger;

}