package com.nbcamp.minishop.dto.order;

import com.nbcamp.minishop.domain.Order;

import java.time.LocalDateTime;

public record OrderResponse(
        Long orderId,
        Long productId,
        String productName,
        int quantity,
        String status,
        LocalDateTime createdAt
) {
    public static OrderResponse from(Order o) {
        return new OrderResponse(
                o.getOrderId(),
                o.getProduct().getProductId(),
                o.getProduct().getName(),
                o.getQuantity(),
                o.getStatus().name(),
                o.getCreatedAt()
        );
    }
}