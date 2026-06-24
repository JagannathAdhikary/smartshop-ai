package com.smartshop.product.model;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "product_read_model")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductReadModel {
    @Id
    private String id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String sellerEmail;
    private Boolean inStock;
    private Set<String> categoryNames;

}
