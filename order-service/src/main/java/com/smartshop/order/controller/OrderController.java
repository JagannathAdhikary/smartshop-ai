package com.smartshop.order.controller;

import com.smartshop.order.dto.OrderRequest;
import com.smartshop.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity order(
            @Valid @RequestBody OrderRequest request,
            @RequestHeader("X-User-Id") String buyerEmail
    ) {
        service.placeOrder(request, buyerEmail);
        return ResponseEntity.ok("Order created");
    }

    @GetMapping("/{id}")
    public ResponseEntity fetchOrder(@PathVariable(name = "id") UUID orderId) {
        return ResponseEntity.ok(service.fetchOrder(orderId));
    }

    @GetMapping("/my")
    public ResponseEntity fetchMyOrdes(@RequestHeader("X-User-Id") String buyerId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.fetchMyOrder(buyerId));
    }

    @PostMapping("/{id}/checkout")
    public ResponseEntity checkout(@PathVariable(name = "id") UUID orderId) {
        return service.checkout(orderId) ?
                ResponseEntity.ok("Payment Initiated") :
                ResponseEntity.internalServerError().body("Cannot proceed with this order");
    }
}
