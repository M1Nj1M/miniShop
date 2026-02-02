package com.nbcamp.minishop.repository;

import com.nbcamp.minishop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 특정 상품의 주문 목록
    List<Order> findAllByProduct_ProductId(Long productId);
}
