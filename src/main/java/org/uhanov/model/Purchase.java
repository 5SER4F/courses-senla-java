package org.uhanov.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "purchase")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Purchase {
    @Id
    @GeneratedValue(generator = "uuid-generator")
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;
    @Column(name = "final_cost", nullable = false)
    private BigDecimal cost;
    @Column(name = "purchase_date")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private LocalDateTime purchaseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "uuid NOT NULL")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", columnDefinition = "uuid NOT NULL")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Product product;

}