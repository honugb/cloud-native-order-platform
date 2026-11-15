package com.honugb.order_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.honugb.order_service.repository.OrderRepository;

@SpringBootTest(properties = {
    "spring.mongodb.uri=mongodb://localhost:27017/orderdb"
})
class OrderServiceApplicationTests {

    @MockitoBean
    private OrderRepository orderRepository;

    @Test
    void contextLoads() {
    }
}