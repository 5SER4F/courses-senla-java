package org.uhanov.model.purchase;

import lombok.*;
import org.uhanov.model.Customer;
import org.uhanov.model.product.Product;

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
    @Column(name = "final_cost", nullable = false, updatable = false)
    private BigDecimal finalCost;
    @Column(name = "purchase_date", updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private LocalDateTime purchaseDate;
    @Column(name = "purchase_status")
    @Enumerated(EnumType.STRING)
    private PurchaseStatus purchaseStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", columnDefinition = "uuid NOT NULL", updatable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Customer buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", columnDefinition = "uuid NOT NULL", updatable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Product product;

    public static final long PERIOD_OF_REFUND = 14;

    public boolean isCanBeRefund() {
        return LocalDateTime.now()
                .minusDays(PERIOD_OF_REFUND)
                .compareTo(purchaseDate) >= 0;
    }
}