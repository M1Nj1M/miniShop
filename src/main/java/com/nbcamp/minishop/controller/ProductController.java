package com.nbcamp.minishop.controller;

import com.nbcamp.minishop.domain.Product;
import com.nbcamp.minishop.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Validated
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
        Product product = productService.create(request.name(), request.price(), request.stock());
        return ResponseEntity
                .created(URI.create("/api/products/" + product.getProductId()))
                .body(ProductResponse.from(product));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> get(@PathVariable Long productId) {
        Product product = productService.get(productId);
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        List<ProductResponse> list = productService.getAll().stream()
                .map(ProductResponse::from)
                .toList();
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        Product updated = productService.update(
                productId,
                request.name(),
                request.price(),
                request.stock()
        );
        return ResponseEntity.ok(ProductResponse.from(updated));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Long productId) {
        productService.delete(productId); // soft delete
        return ResponseEntity.noContent().build();
    }

    // ===================== DTO =====================

    public record CreateProductRequest(
            @NotBlank String name,
            @Min(0) int price,
            @Min(0) int stock
    ) {}

    /**
     * 부분 수정용: null이면 변경 안 함
     */
    public record UpdateProductRequest(
            String name,
            @Min(0) Integer price,
            @Min(0) Integer stock
    ) {}

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
}
