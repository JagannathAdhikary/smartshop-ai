package com.smartshop.product.controller;

import com.smartshop.product.model.ProductReadModel;
import com.smartshop.product.repository.ProductReadRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products/fast-search")
@RequiredArgsConstructor
public class ProductSearchController {

    private final ProductReadRepository repository;

    @GetMapping
    public List<ProductReadModel> searchProduct(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        if(StringUtils.isEmpty(name)) {
            return repository.findAll(pageable).stream().toList();
        }
        return repository.findByNameContainingIgnoreCase(name, pageable);
    }
}
