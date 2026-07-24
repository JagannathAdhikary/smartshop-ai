package com.smartshop.product.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartshop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderStockManager {
    private final ProductService productService;
    private final ProductEventProducer productEventProducer;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-event", groupId = "product-reserver")
    public void reserve(OrderEvent orderEvent) {
        OrderEvent event = null;
        try {
            event = objectMapper.readValue(orderEvent, OrderEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if(event!=null && event.eventType().equals("CREATED")) {
            List<ReservationSummary.Item> reservationItems = new ArrayList<>();
            event.items().stream().forEach(item -> {
                reservationItems.add(
                        new ReservationSummary.Item(
                                item.productId(),
                                this.productService.reserveStock(item.productId(), item.quantity())
                        )
                );
            });
            ReservationSummary summary = new ReservationSummary(
                    event.orderId(),
                    event.buyerEmail(),
                    reservationItems
            );
            productEventProducer.publish(summary);
        }
    }
}
