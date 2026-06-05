package com.smartshop.product.service;

import com.smartshop.product.dto.CategoryRequest;
import com.smartshop.product.model.Category;
import com.smartshop.product.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void addCategory(CategoryRequest request) {
        categoryRepository.save(this.convertToCategory(request));
    }

    private Category convertToCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        return category;
    }
}
