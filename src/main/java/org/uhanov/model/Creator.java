package org.uhanov.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "creator")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Creator {
    @Id
    @GeneratedValue(generator = "uuid-generator")
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "registration_date")
    @EqualsAndHashCode.Exclude
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "creator")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Product> products;
}
