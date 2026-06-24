package com.smartshop.product.service;

import com.smartshop.product.dto.ProductRequest;
import com.smartshop.product.dto.ProductResponse;
import com.smartshop.product.event.ProductEventProducer;
import com.smartshop.product.exceptions.ProductNotFoundException;
import com.smartshop.product.model.Category;
import com.smartshop.product.model.Product;
import com.smartshop.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductEventProducer productEventProducer;

    @CacheEvict(value = "all-products", allEntries = true)
    public void addProduct(ProductRequest request, String email) {
        Product savedProduct = productRepository.save(this.convertToProduct(request, email));
        productEventProducer.publish(savedProduct, "CREATED");
    }

    @Cacheable(value = "products", key = "#p0")
    public ProductResponse fetchProductById(UUID prodId) {
        return convertToProductResponse(this.findById(prodId));
    }

    @Cacheable(
            value = "all-products",
            key = "#p0.getPageNumber() + ':' + #p0.getPageSize() + ':' + #p0.getSort().toString()"
    )
    public List<ProductResponse> fetchProducts(Pageable pageable) {
        return productRepository.findAll(pageable).stream()
                .map(this::convertToProductResponse).toList();
    }

    public List<ProductResponse> fetchProducts(String name, Pageable pageable) {
        return productRepository.findByNameLikeIgnoreCase("%"+name+"%", pageable).stream()
                .map(this::convertToProductResponse).toList();
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "all-products", allEntries = true),
                    @CacheEvict(value = "products", key = "#p0")
            })
//    @CachePut(value = "products", key = "#p0") // As this method is not returning anything so put won't work
    public void updateProduct(UUID prodId, ProductRequest request, String email) {
        Product product = this.findById(prodId);
        if(!product.getSellerEmail().equals(email)) {
            throw new IllegalCallerException("Product doesn't belong to the sender");
        }
        Product savedProduct = productRepository.save(this.updateProductWithRequest(product, request));
        productEventProducer.publish(savedProduct, "UPDATED");
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "all-products", allEntries = true),
                    @CacheEvict(value = "products", key = "#p0")
            }
    )
    @Transactional
    public void deleteProduct(UUID prodId, String email) {
        Product product = this.findById(prodId);
        if(!product.getSellerEmail().equals(email)) {
            throw new IllegalCallerException("Product doesn't belong to the sender");
        }
        productRepository.delete(product);
        productEventProducer.publish(product, "DELETED");
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
