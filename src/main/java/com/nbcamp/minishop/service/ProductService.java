package com.nbcamp.minishop.service;

import com.nbcamp.minishop.domain.Product;
import com.nbcamp.minishop.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product create(String name, int price, int stock) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name is required");
        if (price < 0) throw new IllegalArgumentException("price must be >= 0");
        if (stock < 0) throw new IllegalArgumentException("stock must be >= 0");

        Product product = Product.builder()
                .name(name)
                .price(price)
                .stock(stock)
                .deleted(false)
                .build();

        return productRepository.save(product);
    }

    public Product get(Long productId) {
        return productRepository.findById(productId)
                .filter(p -> !p.isDeleted()) // deleted 필드의 getter는 isDeleted()
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productId));
    }

    public List<Product> getAll() {
        // 간단 버전: 전체 조회 후 deleted 제외
        return productRepository.findAll().stream()
                .filter(p -> !p.isDeleted())
                .toList();
    }

    @Transactional
    public Product update(Long productId, String name, Integer price, Integer stock) {
        Product product = get(productId);

        if (price != null && price < 0) throw new IllegalArgumentException("price must be >= 0");
        if (stock != null && stock < 0) throw new IllegalArgumentException("stock must be >= 0");

        // Setter 대신 도메인 메서드 사용
        product.update(name, price, stock);
        return product;
    }

    @Transactional
    public void delete(Long productId) {
        Product product = get(productId);
        product.softDelete(); // soft delete
    }

    /**
     * 주문 생성에서 사용할 재고 차감 메서드
     */
    @Transactional
    public Product decreaseStock(Long productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be positive");

        Product product = get(productId);
        if (product.getStock() < quantity) {
            throw new IllegalStateException("Not enough stock. current=" + product.getStock());
        }

        product.decreaseStock(quantity);
        return product;
    }

    /**
     * 주문 취소 시 재고 복구가 필요하면 사용
     */
    @Transactional
    public void increaseStock(Long productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be positive");
        Product product = get(productId);
        product.increaseStock(quantity);
    }
}
