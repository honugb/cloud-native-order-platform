package com.honugb.order_service.controller;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.honugb.order_service.dto.OrderRequest;
import com.honugb.order_service.model.Order;
import com.honugb.order_service.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping("/status")
    public Map<String, String> status() {
        return Map.of(
            "application", "Cloud Native Order Platform",
            "status", "RUNNING",
            "version", "1.0.0"
        );
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @Valid @RequestBody OrderRequest request) {

        Order created = service.createOrder(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(created);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return service.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable String id) {
        return service.getOrderById(id);
    }

    @PutMapping("/{id}")
    public Order updateOrder(
            @PathVariable String id,
            @Valid @RequestBody OrderRequest request) {

        return service.updateOrder(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable String id) {

        service.deleteOrder(id);

        return ResponseEntity.noContent().build();
    }
}