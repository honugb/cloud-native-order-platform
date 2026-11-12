package com.honugb.order_service.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.honugb.order_service.dto.OrderRequest;
import com.honugb.order_service.exception.OrderNotFoundException;
import com.honugb.order_service.model.Order;
import com.honugb.order_service.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public Order createOrder(OrderRequest request) {

        Order order = new Order();

        applyDetails(order, request);

        order.setStatus("CREATED");
        order.setCreatedAt(Instant.now());

        return repository.save(order);
    }

    public List<Order> getAllOrders() {
        return repository.findAll();
    }

    public Order getOrderById(String id) {
        return repository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));
    }

    public Order updateOrder(String id, OrderRequest request) {

        Order order = getOrderById(id);

        applyDetails(order, request);

        return repository.save(order);
    }

    public void deleteOrder(String id) {

        Order order = getOrderById(id);

        repository.delete(order);
    }

    private void applyDetails(Order order, OrderRequest request) {

        order.setProductName(request.productName().trim());
        order.setQuantity(request.quantity());
        order.setUnitPrice(request.unitPrice());

        BigDecimal total = request.unitPrice()
            .multiply(BigDecimal.valueOf(request.quantity()));

        order.setTotalAmount(total);
    }
}