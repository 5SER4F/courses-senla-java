package org.uhanov.model;


import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.uhanov.model.purchase.Purchase;
import org.uhanov.model.user.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "customer")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Customer extends User implements UserDetails {
    @Column(name = "firstname")
    private String firstname;
    @Column(name = "balance")
    private Double balance = 0.0;
    @Column(name = "surname")
    private String surname;
    @Column(name = "nickname", nullable = false)
    private String nickname;
    @Column(name = "birth_date", nullable = false, updatable = false)
    private LocalDate birthDate;
    @Column(name = "country", nullable = false)
    private String country;

    @OneToMany(
            mappedBy = "buyer",
            fetch = FetchType.LAZY
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Purchase> userPurchase;

    public double changeBalance(double changes) {
        balance += changes;
        return balance;
    }

}
