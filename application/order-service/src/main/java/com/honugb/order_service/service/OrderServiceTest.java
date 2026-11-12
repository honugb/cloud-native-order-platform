package com.honugb.order_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.honugb.order_service.dto.OrderRequest;
import com.honugb.order_service.exception.OrderNotFoundException;
import com.honugb.order_service.model.Order;
import com.honugb.order_service.repository.OrderRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderRepository repository;
    private OrderService service;

    @BeforeEach
    void setUp() {
        repository = mock(OrderRepository.class);
        service = new OrderService(repository);
    }

    @Test
    void shouldCreateOrderAndCalculateTotal() {

        OrderRequest request = new OrderRequest(
            "Laptop",
            2,
            new BigDecimal("45000.00")
        );

        when(repository.save(any(Order.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = service.createOrder(request);

        assertEquals("Laptop", result.getProductName());
        assertEquals(2, result.getQuantity());
        assertEquals(
            0,
            new BigDecimal("90000.00")
                .compareTo(result.getTotalAmount())
        );
        assertEquals("CREATED", result.getStatus());
        assertNotNull(result.getCreatedAt());

        verify(repository).save(any(Order.class));
    }

    @Test
    void shouldReturnAllOrders() {

        when(repository.findAll())
            .thenReturn(List.of(new Order(), new Order()));

        List<Order> result = service.getAllOrders();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void shouldReturnOrderById() {

        Order order = new Order();
        order.setId("order-123");

        when(repository.findById("order-123"))
            .thenReturn(Optional.of(order));

        Order result = service.getOrderById("order-123");

        assertEquals("order-123", result.getId());
    }

    @Test
    void shouldThrowExceptionForMissingOrder() {

        when(repository.findById("missing"))
            .thenReturn(Optional.empty());

        assertThrows(
            OrderNotFoundException.class,
            () -> service.getOrderById("missing")
        );
    }

    @Test
    void shouldUpdateExistingOrder() {

        Order existing = new Order();
        existing.setId("order-123");

        when(repository.findById("order-123"))
            .thenReturn(Optional.of(existing));

        when(repository.save(any(Order.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        OrderRequest request = new OrderRequest(
            "Keyboard",
            3,
            new BigDecimal("1200.00")
        );

        Order updated = service.updateOrder("order-123", request);

        assertEquals("Keyboard", updated.getProductName());
        assertEquals(3, updated.getQuantity());
        assertEquals(
            0,
            new BigDecimal("3600.00")
                .compareTo(updated.getTotalAmount())
        );
    }

    @Test
    void shouldDeleteExistingOrder() {

        Order existing = new Order();
        existing.setId("order-123");

        when(repository.findById("order-123"))
            .thenReturn(Optional.of(existing));

        service.deleteOrder("order-123");

        verify(repository).delete(existing);
    }
}