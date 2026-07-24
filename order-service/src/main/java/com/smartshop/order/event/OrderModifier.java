package com.smartshop.order.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartshop.order.model.Order;
import com.smartshop.order.model.OrderItem;
import com.smartshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrderModifier {

    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    @KafkaListener(topics = "reservation-events", groupId = "order-modifier")
    public void modify(String event) {
        OrderSummary summary = null;
        try {
            summary = objectMapper.readValue(event, OrderSummary.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if(summary!=null) {
            BigDecimal deductAmount = new BigDecimal(0);
            Set<UUID> unreserved = summary.items().stream()
                    .filter(item -> !item.reservationStatus())
                    .map(OrderSummary.Item::productId).collect(Collectors.toSet());
            if(!unreserved.isEmpty()) {
                Order order = orderService.fetchOrder(summary.orderId());
                for(OrderItem item : order.getOrderItems()){
                    if(unreserved.contains(item.getProductId())) {
                        deductAmount = deductAmount.add(item.getUnitPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity())));
                        item.setQuantity(0);
                    }
                }
                order.setTotalAmount(order.getTotalAmount().subtract(deductAmount));
                order.setUpdatedAt(Instant.now());
                orderService.updateOrder(order);
            }
        }
    }
}
