package org.uhanov.model;

import lombok.*;

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
    @Column(name = "date_added")
    private LocalDate dateAdded;
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    @Column(name = "discount")
    private double discount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Creator creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "age_rating_id", columnDefinition = "uuid NOT NULL")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private AgeRating ageRating;


    @ManyToMany()
    @JoinTable(
            name = "product_genre",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Genre> genres;

    public double getPrice() {
        if (price == null)
            return 0;
        return price.doubleValue();
    }

    public double countDiscountInMoney() {
        return getPrice() * discount;
    }

    public double getFinalPrice() {
        return getPrice() - countDiscountInMoney();
    }


}
