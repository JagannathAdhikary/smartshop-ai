package com.smartshop.order.event;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderEvent(
        UUID orderId,
        String buyerEmail,
        BigDecimal totalAmount,
        List<OrderItemDetail> items,
        String eventType
) {
    public record OrderItemDetail(
            UUID productId,
            String productName,
            int quantity,
            BigDecimal price
    ){}
}
