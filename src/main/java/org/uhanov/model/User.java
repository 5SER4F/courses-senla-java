package org.uhanov.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "\"user\"")//???? Нужны кавычки "\"user\""
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements EntityWithUUID {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    private LocalDateTime registrationDate;
    @Column(name = "country", nullable = false)
    private String country;

    @OneToMany(mappedBy = "buyer")
    private List<Purchase> userPurchase;

    public double changeBalance(double transfer) {
        balance += transfer;
        return balance;
    }

}
