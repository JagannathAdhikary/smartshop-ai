package com.smartshop.order.repository;

import com.smartshop.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    public List<Order> findAllByBuyerId(String email);
}
