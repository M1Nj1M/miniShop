package com.nbcamp.minishop.controller;

import com.nbcamp.minishop.domain.Order;
import com.nbcamp.minishop.domain.OrderStatus;
import com.nbcamp.minishop.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        Order order = orderService.create(request.productId(), request.quantity());
        return ResponseEntity
                .created(URI.create("/api/orders/" + order.getOrderId()))
                .body(OrderResponse.from(order));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> get(@PathVariable Long orderId) {
        Order order = orderService.get(orderId);
        return ResponseEntity.ok(OrderResponse.from(order));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAll() {
        List<OrderResponse> list = orderService.getAll().stream()
                .map(OrderResponse::from)
                .toList();
        return ResponseEntity.ok(list);
    }

    /**
     * 주문 취소: status -> CANCELED
     * (서비스에서 재고 복구까지 하는 버전 기준)
     */
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancel(@PathVariable Long orderId) {
        Order canceled = orderService.cancel(orderId);
        return ResponseEntity.ok(OrderResponse.from(canceled));
    }

    // ===================== DTO =====================

    public record CreateOrderRequest(
            @NotNull Long productId,
            @Min(1) int quantity
    ) {}

    public record OrderResponse(
            Long orderId,
            Long productId,
            int quantity,
            OrderStatus status,
            LocalDateTime createdAt
    ) {
        public static OrderResponse from(Order o) {
            return new OrderResponse(
                    o.getOrderId(),
                    o.getProductId(),
                    o.getQuantity(),
                    o.getStatus(),
                    o.getCreatedAt()
            );
        }
    }
}
