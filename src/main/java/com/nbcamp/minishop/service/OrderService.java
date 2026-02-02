package com.nbcamp.minishop.service;

import com.nbcamp.minishop.domain.Order;
import com.nbcamp.minishop.domain.OrderStatus;
import com.nbcamp.minishop.domain.Product;
import com.nbcamp.minishop.repository.OrderRepository;
import com.nbcamp.minishop.repository.ProductRepository;
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
    private final ProductRepository productRepository;
    private final ProductService productService;

    public Order get(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));
    }

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order create(Long productId, int quantity) {
        // 재고 원자성 -> 락 걸고 조회
        Product product = productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        // 재고 차감 (재고 부족하면 예외)
        product.decreaseStock(quantity);

        // 주문 생성
        Order order = Order.create(product, quantity);
        return orderRepository.save(order);
    }

    @Transactional
    public Order cancel(Long orderId) {
        Order order = get(orderId);

        if (order.getStatus() == OrderStatus.CANCELED) return order;

        // 취소 처리
        order.cancel();

        // 재고 복구 원자적으로 처리 -> 같은 상품 row 락
        Long productId = order.getProduct().getProductId(); // 선택지 A 기준
        Product product = productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        product.increaseStock(order.getQuantity());

        return order;
    }
}
