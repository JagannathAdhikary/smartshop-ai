package com.smartshop.order.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer quantity,
        String sellerEmail,
        List<UUID> categories
) {
}
