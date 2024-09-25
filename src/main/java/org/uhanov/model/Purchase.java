package org.uhanov.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "purchase")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Purchase implements EntityWithUUID {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
//    @Column(name = "user_id", nullable = false)
//    private UUID userId;
    @Column(name = "product_id", nullable = false)
    private UUID productId;
    @Column(name = "final_cost", nullable = false)
    private BigDecimal cost;
    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User buyer;




}