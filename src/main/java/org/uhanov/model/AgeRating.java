package org.uhanov.model;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "age_rating")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AgeRating {
    @Id
    @GeneratedValue(generator = "uuid-generator")
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "add_by", columnDefinition = "uuid NOT NULL")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Staff lastChanger;


}