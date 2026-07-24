package com.smartshop.order.service;

import com.smartshop.order.dto.OrderRequest;
import com.smartshop.order.dto.ProductResponse;
import com.smartshop.order.event.OrderEventProducer;
import com.smartshop.order.exception.InsufficientStockException;
import com.smartshop.order.model.Order;
import com.smartshop.order.model.OrderItem;
import com.smartshop.order.model.OrderStatus;
import com.smartshop.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final RestClient restClient;
    private final OrderEventProducer orderEventProducer;

    public void placeOrder(OrderRequest request, String buyerEmail) {
        List<OrderItem> orderItems = new ArrayList<>();
        Order order = Order.builder()
                .buyerId(buyerEmail)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .status(OrderStatus.PENDING)
                .build();
        BigDecimal totalAmount = new BigDecimal(0);
        // fetch products from product service
        for(var item: request.items()) {
            ProductResponse product = restClient
                    .get()
                    .uri("/api/products/{id}", item.productId())
                    .retrieve()
                    .body(ProductResponse.class);
            if(product.quantity()<item.quantity()) {
                throw new InsufficientStockException("Insufficient stock for product "+ product.id());
            }
            orderItems.add(OrderItem.builder()
                    .productId(product.id())
                    .productName(product.name())
                    .unitPrice(product.price())
                    .quantity(item.quantity())
                    .order(order)
                    .build());
            totalAmount = totalAmount.add(product.price().multiply(BigDecimal.valueOf(item.quantity())));
        }
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        repository.save(order);
        orderEventProducer.publish(order, "CREATED");
    }

    public Order fetchOrder(UUID orderId) {
        return repository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order Not Found"));
    }

    public List<Order> fetchMyOrder(String buyerEmail) {
        return repository.findAllByBuyerId(buyerEmail);
    }

    public void updateOrder(Order order) {
        repository.save(order);
    }

    public boolean checkout(UUID orderId) {
        try {
            Order order = this.fetchOrder(orderId);
            order.transitTo(OrderStatus.PAYMENT_PROCESSING);
            orderEventProducer.publish(order, "PAYMENT_PROCESSING");
            return true;
        } catch(IllegalStateException ex) {
            return false;
        }
    }
}
