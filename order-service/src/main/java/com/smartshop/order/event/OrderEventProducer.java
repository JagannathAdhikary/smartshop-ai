package com.smartshop.order.event;

import com.smartshop.order.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    private final String TOPIC = "order-event";

    public void publish(Order order, String orderType) {
        OrderEvent event = new OrderEvent(
                order.getId(),
                order.getBuyerId(),
                order.getTotalAmount(),
                order.getOrderItems().stream().map(item ->
                        new OrderEvent.OrderItemDetail(
                                item.getProductId(),
                                item.getProductName(),
                                item.getQuantity(),
                                item.getUnitPrice()
                        )).toList(),
                orderType
        );
        kafkaTemplate.send(TOPIC, event.orderId().toString(), event);
    }
}
