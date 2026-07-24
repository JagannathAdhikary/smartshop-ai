package com.smartshop.product.event;

import java.util.List;
import java.util.UUID;

public record ReservationSummary(
    UUID orderId,
    String buyerEmail,
    List<ReservationSummary.Item> items
) {
    public record Item(
            UUID productId,
            boolean reservationStatus
    ) {}
}
