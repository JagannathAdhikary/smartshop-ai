package com.smartshop.product.service;

import com.smartshop.product.dto.ProductRequest;
import com.smartshop.product.dto.ProductResponse;
import com.smartshop.product.exceptions.ProductNotFoundException;
import com.smartshop.product.model.Category;
import com.smartshop.product.model.Product;
import com.smartshop.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public void addProduct(ProductRequest request, String email) {
        productRepository.save(this.convertToProduct(request, email));
    }

    public ProductResponse fetchProductById(UUID prodId) {
        Product product = productRepository.findById(prodId)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
        return convertToProductResponse(product);
    }

    private Product convertToProduct(ProductRequest request, String email) {
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .sellerEmail(email)
                .categories(request.getCategories())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    private ProductResponse convertToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .categories(product.getCategories().stream().map( Category::getId ).toList())
                .sellerEmail(product.getSellerEmail())
                .build();
    }
}
