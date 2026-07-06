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

    private static final String TOPIC = "product-events";

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
}
