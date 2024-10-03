package org.uhanov.model;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "genre")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Genre {
    @Id
    @GeneratedValue(generator = "uuid-generator")
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "add_by", columnDefinition = "uuid NOT NULL", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Staff lastChanger;

}
