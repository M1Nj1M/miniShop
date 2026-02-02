package com.nbcamp.minishop.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    // FK 연관관계로 묶어도 되지만( @ManyToOne ),
    // 지금 테이블이 product_id 컬럼만 있고 단순하니 일단 ID로만 들고 가는 게 가벼움.
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = OrderStatus.CREATED;
    }

    public static Orders create(Long productId, int quantity) {
        return Orders.builder()
                .productId(productId)
                .quantity(quantity)
                .status(OrderStatus.CREATED)
                .build();
    }

    public void cancel() {
        this.status = OrderStatus.CANCELED;
    }
}