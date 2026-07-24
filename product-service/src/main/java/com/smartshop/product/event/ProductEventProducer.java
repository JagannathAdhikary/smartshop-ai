package com.smartshop.product.event;

import com.smartshop.product.model.Category;
import com.smartshop.product.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductEventProducer {
    private final KafkaTemplate<String, ProductUpdateEvent> kafkaTemplate;
    private final KafkaTemplate<String, ReservationSummary> kafkaReservationTemplate;

    private static final String TOPIC = "product-events";
    private static final String RESERVE_TOPIC = "reservation-events";

    public void publish(Product product, String eventType) {
        ProductUpdateEvent event = new ProductUpdateEvent(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getSellerEmail(),
                product.getCategories().stream().map(Category::getName)
                        .collect(Collectors.toSet()),
                eventType
        );
        kafkaTemplate.send(TOPIC, product.getId().toString(), event);
    }

    public void publish(ReservationSummary summary) {
        kafkaReservationTemplate.send(RESERVE_TOPIC, summary.orderId().toString(), summary);
    }
}
