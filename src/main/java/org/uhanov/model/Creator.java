package org.uhanov.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.uhanov.model.product.Product;
import org.uhanov.model.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "creator")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Creator extends User implements UserDetails {
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "balance")
    private Double balance;
    @Column(name = "last_settlement_date")
    @EqualsAndHashCode.Exclude
    protected LocalDateTime lastSettlementDate;

    @OneToMany(mappedBy = "creator")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Product> products;

    public double changeBalance(double changes) {
        balance += changes;
        return balance;
    }

}
