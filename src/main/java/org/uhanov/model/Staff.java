package org.uhanov.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "staff")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Staff {
    @Id
    @GeneratedValue(generator = "uuid-generator")
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "firstname", nullable = false)
    private String firstname;
    @Column(name = "surname", nullable = false)
    private String surname;
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
    @Column(name = "registration_date", nullable = false)
    @EqualsAndHashCode.Exclude
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "lastChanger")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<AgeRating> ageRatingsAddedBy;

    @OneToMany(mappedBy = "lastChanger")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Genre> genresAddedBy;
}
