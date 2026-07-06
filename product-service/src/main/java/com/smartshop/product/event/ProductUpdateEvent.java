package com.smartshop.product.event;

import java.util.Set;
import java.util.UUID;

public record ProductUpdateEvent (
    UUID productId,
    String name,
    String description,
    Double price,
    Integer stock,
    String seller_email,
    Set<String> catergoryNames,
    String eventType
) {}
