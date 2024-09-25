package org.uhanov.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "staff")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Staff implements EntityWithUUID {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    private LocalDateTime registrationDate;
}
