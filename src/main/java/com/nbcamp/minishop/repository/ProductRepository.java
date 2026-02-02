package com.nbcamp.minishop.repository;

import com.nbcamp.minishop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 삭제되지 않은 상품 목록
    List<Product> findAllByDeletedFalse();

    // 삭제되지 않은 상품 단건
    Optional<Product> findByProductIdAndDeletedFalse(Long productId);

    // 상품명으로 검색
    List<Product> findByNameContainingAndDeletedFalse(String keyword);

    // 삭제된 상품 조회
    List<Product> findAllByDeletedTrue();
}
