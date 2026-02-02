package com.nbcamp.minishop.repository;

import com.nbcamp.minishop.domain.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    // 재고 락 조회
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.productId = :id")
    Optional<Product> findByIdForUpdate(@Param("id") Long id);
}
