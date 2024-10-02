package org.uhanov.model;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "\"user\"")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(generator = "uuid-generator")
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "firstname")
    private String firstname;
    @Column(name = "balance")
    private Double balance;
    @Column(name = "surname")
    private String surname;
    @Column(name = "nickname", nullable = false)
    private String nickname;
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
    @Column(name = "registration_date")
    @EqualsAndHashCode.Exclude
    private LocalDateTime registrationDate;
    @Column(name = "country", nullable = false)
    private String country;

    @OneToMany(
            mappedBy = "buyer",
            fetch = FetchType.EAGER
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Purchase> userPurchase;

    public double changeBalance(double transfer) {
        balance += transfer;
        return balance;
    }

}
