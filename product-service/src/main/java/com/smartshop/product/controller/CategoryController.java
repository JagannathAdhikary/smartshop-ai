package com.smartshop.product.controller;

import com.smartshop.product.dto.CategoryRequest;
import com.smartshop.product.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity addCategory(@Valid @RequestBody CategoryRequest request) {
        categoryService.addCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Category created successfully");
    }
}
