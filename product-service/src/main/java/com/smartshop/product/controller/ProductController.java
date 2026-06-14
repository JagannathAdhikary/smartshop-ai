package com.smartshop.product.controller;

import com.smartshop.product.dto.ProductRequest;
import com.smartshop.product.dto.ProductResponse;
import com.smartshop.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity addProduct(@Valid @RequestBody ProductRequest request,
                                     @RequestHeader("X-User-Id") String email) {
        productService.addProduct(request, email);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");
    }

    @PatchMapping("/{id}")
    public ResponseEntity updateProduct(@PathVariable("id") UUID productId,
                                        @RequestBody ProductRequest request,
                                        @RequestHeader("X-User-Id") String email) {
        productService.updateProduct(productId, request, email);
        return ResponseEntity.ok("Product updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable("id") UUID productId,
                                        @RequestHeader("X-User-Id") String email) {
        productService.deleteProduct(productId, email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity fetchAllProducts(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size
    ) {
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        if(name!=null) {
            return ResponseEntity.ok(productService.fetchProducts(name, pageable));
        } else {
            return ResponseEntity.ok(productService.fetchProducts(pageable));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity fetchProducts(@PathVariable("id") UUID productId) {
        ProductResponse response = productService.fetchProductById(productId);
        return ResponseEntity.ok(response);
    }
}
