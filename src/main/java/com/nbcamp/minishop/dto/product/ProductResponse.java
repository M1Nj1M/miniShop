package com.nbcamp.minishop.dto.product;

import com.nbcamp.minishop.domain.Product;

import java.time.LocalDateTime;

public record ProductResponse(
        Long productId,
        String name,
        int price,
        int stock,
        boolean deleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProductResponse from(Product p) {
        return new ProductResponse(
                p.getProductId(),
                p.getName(),
                p.getPrice(),
                p.getStock(),
                p.isDeleted(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}
