package com.nbcamp.minishop.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (!this.deleted) this.deleted = false;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String name, Integer price, Integer stock) {
        if (name != null) this.name = name;
        if (price != null) this.price = price;
        if (stock != null) this.stock = stock;
    }

    public void softDelete() {
        this.deleted = true;
    }

    public void decreaseStock(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("quantity must be positive");
        this.stock -= qty;
    }

    public void increaseStock(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("quantity must be positive");
        this.stock += qty;
    }
}