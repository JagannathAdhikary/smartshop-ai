package com.smartshop.order.model;

public enum OrderStatus {
    PENDING,
    PAYMENT_PROCESSING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    REPLACEMENT_INITIATED,
    REFUND_INITIATED,
    REFUNDED,
    REPLACED,
    COMPLETED,
    FAILED;

    public boolean canTransitTo(OrderStatus status) {
        return switch(this) {
            case PENDING -> status == PAYMENT_PROCESSING || status == FAILED;
            case PAYMENT_PROCESSING -> status == CONFIRMED || status == FAILED;
            case CONFIRMED -> status == SHIPPED || status == CANCELLED;
            case SHIPPED -> status == DELIVERED || status == CANCELLED;
            case DELIVERED -> status == REPLACEMENT_INITIATED || status == REFUND_INITIATED || status == COMPLETED;
            case CANCELLED -> status == REFUND_INITIATED;
            case REPLACEMENT_INITIATED -> status == REPLACED;
            case REFUND_INITIATED -> status == REFUNDED;
            case REFUNDED, REPLACED -> status == COMPLETED;
            case COMPLETED, FAILED -> false;
        };
    }
}
