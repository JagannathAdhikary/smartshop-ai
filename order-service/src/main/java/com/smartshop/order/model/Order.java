package com.smartshop.order.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String buyerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    private Instant createdAt;

    private Instant updatedAt;

    public void transitTo(OrderStatus status) {
        if(!this.status.canTransitTo(status)) {
            throw new IllegalStateException("Transition from "+this.status+" to "+status+" is not possible");
        }
        this.status = status;
        this.updatedAt = Instant.now();
    }


}
