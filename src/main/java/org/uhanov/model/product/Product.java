package org.uhanov.model.product;

import lombok.*;
import org.uhanov.model.AgeRating;
import org.uhanov.model.Creator;
import org.uhanov.model.Genre;
import org.uhanov.model.purchase.Purchase;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "product")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(generator = "uuid-generator")
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "date_added", updatable = false)
    private LocalDate dateAdded;
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    @Column(name = "discount")
    private Double discount = 0.0;
    @Column(name = "age_rating", nullable = false)
    @Enumerated(EnumType.STRING)
    private AgeRating ageRating;
    @Column(name = "product_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Creator creator;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_genre",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Genre> genres;

    @OneToMany(
            mappedBy = "product",
            fetch = FetchType.LAZY
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Purchase> purchaseOfProduct;

    public double getPrice() {
        if (price == null)
            return 0;
        return price.doubleValue();
    }

    public boolean isUserPassAgeFilter(LocalDate userBirthDate) {
        return ageRating.isPassAgeFilter(userBirthDate);
    }

    public double countDiscountInMoney() {
        return getPrice() * discount;
    }

    public double getFinalPrice() {
        return getPrice() - countDiscountInMoney();
    }

}
