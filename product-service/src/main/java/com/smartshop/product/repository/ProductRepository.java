package com.smartshop.product.repository;

import com.smartshop.product.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByNameLikeIgnoreCase(String name, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Product set quantity = quantity - :q where id = :id and quantity >= :q")
    int reserveStock(@Param("id") UUID id, @Param("q") int q);
}
