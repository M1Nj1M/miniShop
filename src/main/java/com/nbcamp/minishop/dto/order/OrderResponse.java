package com.nbcamp.minishop.dto.order;

import com.nbcamp.minishop.domain.Order;
import com.nbcamp.minishop.dto.product.ProductResponse;

import java.time.LocalDateTime;

public record OrderResponse(
        Long orderId,
        Long productId,
        int quantity,
        String status,
        LocalDateTime createdAt,
        ProductResponse product   // 주문 조회 시 상품 정보까지 포함
) {
    public static OrderResponse from(Order o, ProductResponse product) {
        return new OrderResponse(
                o.getOrderId(),
                o.getProductId(),
                o.getQuantity(),
                o.getStatus().name(),   // OrderStatus enum이면 name()
                o.getCreatedAt(),
                product
        );
    }

    // 상품 정보 없이도 만들 수 있게 오버로드(옵션)
    public static OrderResponse from(Order o) {
        return new OrderResponse(
                o.getOrderId(),
                o.getProductId(),
                o.getQuantity(),
                o.getStatus().name(),
                o.getCreatedAt(),
                null
        );
    }
}
