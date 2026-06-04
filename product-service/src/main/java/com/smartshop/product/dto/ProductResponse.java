package com.smartshop.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
public class ProductResponse {
    private UUID id;

    private String name;

    private String description;

    private Double price;

    private int quantity;

    private String sellerEmail;

    private List<UUID> categories;
}
