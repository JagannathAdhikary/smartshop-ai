package com.smartshop.product.event;

import com.smartshop.product.model.ProductReadModel;
import com.smartshop.product.repository.ProductReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductReadModelProjector {

    private final ProductReadRepository productReadRepository;

    @KafkaListener(topics = "product-events", groupId = "read-product-projector")
    public void project(ProductUpdateEvent event) {
        if(event.eventType().equals("DELETED")) {
            productReadRepository.deleteById(event.productId().toString());
            log.info("Deleted from mongodb <Product ID: "+event.productId()+">");
            return;
        }
        ProductReadModel productReadModel =
                ProductReadModel.builder()
                        .id(event.productId().toString())
                        .name(event.name())
                        .description(event.description())
                        .quantity(event.stock())
                        .price(event.price())
                        .sellerEmail(event.seller_email())
                        .inStock(event.stock()>0)
                        .categoryNames(event.catergoryNames())
                        .build();
        productReadRepository.save(productReadModel);
        log.info("{} product {} in mongodb", event.eventType(), event.productId());
    }

}
