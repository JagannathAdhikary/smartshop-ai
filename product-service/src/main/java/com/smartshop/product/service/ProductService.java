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
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public void addProduct(ProductRequest request, String email) {
        productRepository.save(this.convertToProduct(request, email));
    }

    public ProductResponse fetchProductById(UUID prodId) {
        return convertToProductResponse(this.findById(prodId));
    }

    public List<ProductResponse> fetchProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToProductResponse).toList();
    }

    public List<ProductResponse> fetchProducts(String name) {
        return productRepository.findByNameLikeIgnoreCase("%"+name+"%").stream()
                .map(this::convertToProductResponse).toList();
    }

    public void updateProduct(UUID prodId, ProductRequest request, String email) {
        Product product = this.findById(prodId);
        if(!product.getSellerEmail().equals(email)) {
            throw new IllegalCallerException("Product doesn't belong to the sender");
        }
        productRepository.save(this.updateProductWithRequest(product, request));
    }

    public void deleteProduct(UUID prodId, String email) {
        Product product = this.findById(prodId);
        if(!product.getSellerEmail().equals(email)) {
            throw new IllegalCallerException("Product doesn't belong to the sender");
        }
        productRepository.delete(product);
    }

    private Product findById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
    }
    private Product updateProductWithRequest(Product product, ProductRequest request) {
        if(request.getName() != null) product.setName(request.getName());
        if(request.getDescription() != null) product.setDescription(request.getDescription());
        if(request.getQuantity() != null) product.setQuantity(request.getQuantity());
        if(request.getPrice() != null) product.setPrice(request.getPrice());
        if(request.getCategories() != null) product.setCategories(request.getCategories());
        return product;
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
