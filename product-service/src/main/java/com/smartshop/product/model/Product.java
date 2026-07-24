package com.smartshop.product.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Double price;

    @NotNull
    @Min(value = 0, message = "Stock cannot be less than 0")
    private Integer quantity;

    @NotBlank
    @Column(name="seller_email")
    private String sellerEmail;

    @ManyToMany
    private Set<Category> categories;

    private Instant createdAt;

    private Instant updatedAt;
}
