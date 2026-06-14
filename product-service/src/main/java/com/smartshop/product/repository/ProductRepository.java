package com.smartshop.product.repository;

import com.smartshop.product.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByNameLikeIgnoreCase(String name, Pageable pageable);
}
