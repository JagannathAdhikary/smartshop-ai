package com.smartshop.order.event;

import java.util.List;
import java.util.UUID;

public record OrderSummary(
        UUID orderId,
        String buyerEmail,
        List<Item> items
) {
    public record Item(
            UUID productId,
            boolean reservationStatus
    ) {}
}
