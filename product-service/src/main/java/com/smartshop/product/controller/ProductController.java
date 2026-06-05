package com.smartshop.product.controller;

import com.smartshop.product.dto.ProductRequest;
import com.smartshop.product.dto.ProductResponse;
import com.smartshop.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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

    @GetMapping("/{id}")
    public ResponseEntity fetchProducts(@PathVariable("id") UUID productId) {
        ProductResponse response = productService.fetchProductById(productId);
        return ResponseEntity.ok(response);
    }
}
