package com.smartshop.product.repository;

import com.smartshop.product.model.ProductReadModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductReadRepository extends MongoRepository<ProductReadModel, String> {
    public List<ProductReadModel> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
