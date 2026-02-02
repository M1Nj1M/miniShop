package com.nbcamp.minishop.service;

import com.nbcamp.minishop.domain.Order;
import com.nbcamp.minishop.domain.OrderStatus;
import com.nbcamp.minishop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Transactional
    public Order create(Long productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be positive");

        // 재고 차감 + 상품 존재/삭제 여부 검증
        productService.decreaseStock(productId, quantity);

        // new Order() 금지 -> Order.create() 사용
        Order order = Order.create(productId, quantity);
        return orderRepository.save(order);
    }

    public Order get(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));
    }

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order cancel(Long orderId) {
        Order order = get(orderId);

        // 이미 취소면 그대로 반환
        if (order.getStatus() == OrderStatus.CANCELED) return order;

        // Setter 대신 도메인 메서드
        order.cancel();

        // 취소 시 재고 복구를 원하면 복구(선택)
        productService.increaseStock(order.getProductId(), order.getQuantity());

        return order;
    }
}
